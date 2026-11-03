package com.roni.gymcraft.core.game

import com.roni.gymcraft.core.save.SaveStore
import com.roni.gymcraft.engine.content.Matchup
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.economy.Refusal
import com.roni.gymcraft.engine.model.BattleOutcome
import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.model.UnitId
import com.roni.gymcraft.engine.save.SaveFile
import com.roni.gymcraft.engine.waves.Campaign
import com.roni.gymcraft.engine.waves.Forecast
import com.roni.gymcraft.engine.waves.Forecaster
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/** The game as screens see it: the state, how many battles were already shown, the forecast, and when this was. */
data class GameSnapshot(
    val state: GameState,
    val seenBattles: Int,
    val forecast: Forecast?,
    val at: LocalDateTime,
) {
    /** Battles fought since the player last looked: the "While you were away" card. */
    val unseen: List<BattleOutcome> get() = state.battles.drop(seenBattles)
}

sealed interface Opened {
    data object Ok : Opened
    /** The save is from a newer app: nothing is loaded and nothing will be written. */
    data class TooNew(val version: Int) : Opened
    /** Neither the save nor its backup could be read; they were set aside and a new game started. */
    data class StartedFresh(val setAside: List<File>) : Opened
}

/**
 * The one owner of the game (architecture.md §1, §7): loads it, runs every rule through the engine, saves after every
 * change, and publishes a [GameSnapshot]. Every change first fights the battles that fell due, so the order of
 * events is always the calendar's. One change at a time ([Mutex]); disk and simulation run on [io].
 *
 * A failed save throws: the caller's `Failures.launch` shows the error card, and the published game stays the one on
 * disk. Talks to: [SaveStore], the engine.
 */
class GameRepository(
    private val store: SaveStore,
    private val clock: GameClock,
    private val content: GameContent,
    private val matchup: Matchup,
    private val io: CoroutineDispatcher,
    private val seed: () -> Long = System::nanoTime,
) {
    private val mutex = Mutex()
    private val _game = MutableStateFlow<GameSnapshot?>(null)
    val game: StateFlow<GameSnapshot?> = _game.asStateFlow()

    /** Loads the save (or starts a new game), fights what fell due, saves. Safe to call again: then it only refreshes. */
    suspend fun open(): Opened = mutex.withLock {
        withContext(io) {
            if (_game.value != null) { resolveAndCommit(); return@withContext Opened.Ok }
            val now = clock.now()
            val loaded = store.load()
            val (file, result) = when (loaded) {
                is SaveStore.Loaded.TooNew -> return@withContext Opened.TooNew(loaded.version)
                is SaveStore.Loaded.Ok -> loaded.file to Opened.Ok
                SaveStore.Loaded.Empty -> newGame(now) to Opened.Ok
                is SaveStore.Loaded.Unreadable -> newGame(now) to Opened.StartedFresh(loaded.setAside)
            }
            commit(Campaign.resolveDue(file.state, content, now).state, file.seenBattles, now)
            result
        }
    }

    /** Fights what fell due since the last look (the app came back to the front, or a battle time passed). */
    suspend fun refresh() = mutex.withLock { withContext(io) { resolveAndCommit() } }

    suspend fun session(): Change = change { s, today -> Economy.session(s, today, today) }
    suspend fun homeCard(): Change = change { s, today -> Economy.homeCard(s, today, today) }
    suspend fun train(unit: UnitId, count: Int): Change = change { s, _ -> Economy.train(s, content, unit, count) }
    suspend fun upgrade(building: BuildingId): Change = change { s, _ -> Economy.upgrade(s, content, building) }
    suspend fun startLeave(): Change = change { s, today -> Economy.startLeave(s, today) }
    suspend fun endLeave(): Change = change { s, today -> Economy.endLeave(s, today) }

    /** The player read the "While you were away" card. */
    suspend fun markSeen() = mutex.withLock {
        withContext(io) {
            val snap = _game.value ?: return@withContext
            commit(snap.state, snap.state.battles.size, clock.now())
        }
    }

    private suspend fun change(op: (GameState, LocalDate) -> Change): Change = mutex.withLock {
        withContext(io) {
            val snap = _game.value ?: return@withContext Change.Refused(Refusal.UNKNOWN)
            val now = clock.now()
            val resolved = Campaign.resolveDue(snap.state, content, now).state
            val result = op(resolved, now.toLocalDate())
            commit((result as? Change.Ok)?.state ?: resolved, snap.seenBattles, now)
            result
        }
    }

    private fun resolveAndCommit() {
        val snap = _game.value ?: return
        val now = clock.now()
        commit(Campaign.resolveDue(snap.state, content, now).state, snap.seenBattles, now)
    }

    private fun newGame(now: LocalDateTime) =
        SaveFile(Economy.newGame(content, matchup.player, matchup.enemy, now.toLocalDate(), now, seed()))

    /** Saves first, publishes after: what screens see is always what's on disk. */
    private fun commit(state: GameState, seen: Int, now: LocalDateTime) {
        val file = SaveFile(state, seen.coerceAtMost(state.battles.size))
        val current = _game.value
        if (current == null || current.state != state || current.seenBattles != file.seenBattles) store.save(file)
        _game.value = GameSnapshot(state, file.seenBattles, Forecaster.nextBoss(state, content, now), now)
    }
}
