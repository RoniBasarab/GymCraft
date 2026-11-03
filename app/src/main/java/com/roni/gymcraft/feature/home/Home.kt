package com.roni.gymcraft.feature.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roni.gymcraft.core.game.GameRepository
import com.roni.gymcraft.core.game.Opened
import com.roni.gymcraft.core.game.TimeTravel
import com.roni.gymcraft.core.system.DebugTools
import com.roni.gymcraft.core.system.Failures
import com.roni.gymcraft.core.system.ProblemAction
import com.roni.gymcraft.core.system.Rescue
import com.roni.gymcraft.core.system.reportFailures
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.UnitId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

sealed interface HomePhase {
    data object Loading : HomePhase
    data object Ready : HomePhase
    /** The save is from a newer app: nothing loaded, nothing written. */
    data class TooNew(val version: Int) : HomePhase
}

@Immutable
data class HomeState(
    val phase: HomePhase = HomePhase.Loading,
    val game: GameUi? = null,
    /** Something failed: the error card shows above whatever still works. */
    val problem: Boolean = false,
    /** The open building sheet, or null. */
    val sheet: BuildingSheet? = null,
    /** The sergeant's answer to the last tap; shown in his bubble instead of [GameUi.sergeant] until tapped away. */
    val reply: String? = null,
    /** The ⋮ menu (leave; debug: sprite test, time travel) is open. */
    val menuOpen: Boolean = false,
    /** Debug builds: the sprite test and time travel in the menu, and the game's time on screen. */
    val debugTools: Boolean = false,
)

sealed interface HomeAction {
    /** The hold-to-confirm "I WENT" finished. */
    data object Went : HomeAction
    data object CardDone : HomeAction
    data class OpenBuilding(val id: BuildingId) : HomeAction
    data object CloseSheet : HomeAction
    data class Pick(val unit: UnitId, val count: Int) : HomeAction
    data class Train(val unit: UnitId) : HomeAction
    data object Upgrade : HomeAction
    data object StartLeave : HomeAction
    data object EndLeave : HomeAction
    /** "Got it" on the "While you were away" card. */
    data object DismissAway : HomeAction
    data object DismissReply : HomeAction
    data class ShowMenu(val open: Boolean) : HomeAction
    /** The screen came back to the front: fight what fell due meanwhile. */
    data object Resumed : HomeAction
    data object OpenSpriteTest : HomeAction
    data class TimeForward(val minutes: Long) : HomeAction
    data object TimeReset : HomeAction
    data class Problem(val action: ProblemAction) : HomeAction
}

sealed interface HomeEffect {
    data object OpenSpriteTest : HomeEffect
}

/** Home (architecture.md §6). Talks to: [GameRepository], [Failures], [Rescue], [TimeTravel]. */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: GameRepository,
    private val content: GameContent,
    private val failures: Failures,
    private val rescue: Rescue,
    private val time: TimeTravel,
    tools: DebugTools,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState(debugTools = tools.enabled))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>(extraBufferCapacity = 8)
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        repo.game.filterNotNull()
            .onEach { snap ->
                _state.update { s ->
                    s.copy(
                        game = HomeUi.game(snap, content),
                        sheet = s.sheet?.let { open -> HomeUi.sheet(snap, content, open.id, open.picks()) },
                    )
                }
            }
            .reportFailures(failures, "home.game") { _state.update { it.copy(problem = true) } }
            .launchIn(viewModelScope)
        run("home.open") {
            when (val opened = repo.open()) {
                Opened.Ok -> _state.update { it.copy(phase = HomePhase.Ready) }
                is Opened.TooNew -> _state.update { it.copy(phase = HomePhase.TooNew(opened.version)) }
                is Opened.StartedFresh -> _state.update { it.copy(phase = HomePhase.Ready, reply = HomeUi.STARTED_FRESH) }
            }
        }
    }

    fun onAction(action: HomeAction) {
        if (_state.value.menuOpen && action !is HomeAction.ShowMenu) _state.update { it.copy(menuOpen = false) }   // a menu item closes it
        when (action) {
            HomeAction.Went -> run("home.session") {
                reply(when (val c = repo.session()) {
                    is Change.Ok -> SergeantLine.session(c)
                    is Change.Refused -> HomeUi.refused(c.why)
                })
            }
            HomeAction.CardDone -> run("home.card") {
                reply(when (val c = repo.homeCard()) {
                    is Change.Ok -> HomeUi.CARD_DONE
                    is Change.Refused -> HomeUi.refused(c.why)
                })
            }
            is HomeAction.OpenBuilding -> {
                val snap = repo.game.value ?: return
                _state.update { it.copy(sheet = HomeUi.sheet(snap, content, action.id, emptyMap())) }
            }
            HomeAction.CloseSheet -> _state.update { it.copy(sheet = null) }
            is HomeAction.Pick -> {
                val snap = repo.game.value ?: return
                _state.update { s ->
                    s.copy(sheet = s.sheet?.let { open -> HomeUi.sheet(snap, content, open.id, open.picks() + (action.unit to action.count)) })
                }
            }
            is HomeAction.Train -> {
                val row = _state.value.sheet?.units?.firstOrNull { it.id == action.unit } ?: return
                run("home.train") {
                    reply(when (val c = repo.train(row.id, row.count)) {
                        is Change.Ok -> HomeUi.trained(row.name, row.count)
                        is Change.Refused -> HomeUi.refused(c.why)
                    })
                }
            }
            HomeAction.Upgrade -> {
                val sheet = _state.value.sheet ?: return
                run("home.upgrade") {
                    reply(when (val c = repo.upgrade(sheet.id)) {
                        is Change.Ok -> HomeUi.upgraded(sheet, content)
                        is Change.Refused -> HomeUi.refused(c.why)
                    })
                }
            }
            HomeAction.StartLeave -> run("home.leave") {
                val c = repo.startLeave()
                reply(if (c is Change.Refused) HomeUi.refused(c.why) else null)
            }
            HomeAction.EndLeave -> run("home.back") {
                reply(when (val c = repo.endLeave()) {
                    is Change.Ok -> HomeUi.BACK_FROM_LEAVE
                    is Change.Refused -> HomeUi.refused(c.why)
                })
            }
            HomeAction.DismissAway -> run("home.seen") { repo.markSeen() }
            HomeAction.DismissReply -> _state.update { it.copy(reply = null) }
            is HomeAction.ShowMenu -> _state.update { it.copy(menuOpen = action.open) }
            HomeAction.Resumed -> run("home.refresh") { repo.refresh() }
            HomeAction.OpenSpriteTest -> _effects.tryEmit(HomeEffect.OpenSpriteTest)
            is HomeAction.TimeForward -> run("home.time") { time.forward(action.minutes); repo.refresh() }
            HomeAction.TimeReset -> run("home.time") { time.reset(); repo.refresh() }
            is HomeAction.Problem -> rescue.handle(action.action)
        }
    }

    private fun reply(text: String?) = _state.update { it.copy(reply = text) }

    private fun run(where: String, block: suspend () -> Unit) {
        failures.launch(viewModelScope, where, onFailure = { _state.update { it.copy(problem = true) } }) { block() }
    }
}
