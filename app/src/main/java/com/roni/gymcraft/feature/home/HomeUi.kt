package com.roni.gymcraft.feature.home

import androidx.compose.runtime.Immutable
import com.roni.gymcraft.core.game.GameSnapshot
import com.roni.gymcraft.engine.economy.Refusal
import com.roni.gymcraft.engine.model.ArtId
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.RaceId
import com.roni.gymcraft.engine.model.UnitId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

/** Home's picture of the game, built from a [GameSnapshot] by [HomeUi.game]. */
@Immutable
data class GameUi(
    val race: RaceId,
    val supplies: Int,
    /** The drill sergeant's line ([SergeantLine.home]). */
    val sergeant: String,
    val forecast: ForecastUi?,
    val onLeave: Boolean,
    val wentToday: Boolean,
    val card: HomeCardUi,
    /** "While you were away": one line per battle not yet seen. Empty = no card. */
    val away: List<String>,
    val hq: ArtId,
    val buildings: List<BuildingUi>,
    val army: List<SquadUi>,
    /** The game's time, "Sat 7 Nov 21:00": shown only with the debug tools (time travel). */
    val time: String,
)

/** "Boss · Sat 20:00", "30 Biters and 2 Brutes", 62 (%). */
@Immutable
data class ForecastUi(val title: String, val wave: String, val chance: Int)

@Immutable
data class HomeCardUi(val text: String, val done: Boolean)

@Immutable
data class BuildingUi(val id: BuildingId, val name: String, val art: ArtId, val level: Int, val maxLevel: Int, val occupied: Boolean)

/** One unit type of the standing army: [ready] fight, [wounded] are back after the next session. */
@Immutable
data class SquadUi(val unit: UnitId, val name: String, val art: ArtId, val ready: Int, val wounded: Int)

/** The sheet of one production building: its units (train) and its next level (upgrade). */
@Immutable
data class BuildingSheet(
    val id: BuildingId,
    val name: String,
    val art: ArtId,
    val level: Int,
    val maxLevel: Int,
    val occupied: Boolean,
    /** The next level's cost; null at max level. */
    val upgradeCost: Int?,
    val canUpgrade: Boolean,
    val units: List<SheetUnit>,
) {
    fun picks(): Map<UnitId, Int> = units.associate { it.id to it.count }
}

/** A unit row: [count] is the +/− picker (1..[SheetUnit.MAX]); [total] = cost × count. */
@Immutable
data class SheetUnit(
    val id: UnitId,
    val name: String,
    val art: ArtId,
    val cost: Int,
    val tier: Int,
    val locked: Boolean,
    val count: Int,
    val total: Int,
    val canTrain: Boolean,
    val ready: Int,
) {
    companion object {
        const val MAX = 20
    }
}

/** Pure mapping from the engine to Home's state, and the sergeant's answers to a refused tap. Unit-tested. */
object HomeUi {
    private val TIME = DateTimeFormatter.ofPattern("EEE d MMM HH:mm", Locale.ENGLISH)

    fun game(snap: GameSnapshot, content: GameContent): GameUi {
        val s = snap.state
        val today = snap.at.toLocalDate()
        val race = content.race(s.race)
        val onLeave = s.onLeave(today)
        val forecast = snap.forecast?.takeIf { !onLeave }?.let { f ->
            val kind = if (f.battle.kind == BattleKind.BOSS) "Boss" else "Raid"
            val day = f.battle.at.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            ForecastUi("$kind · $day %02d:%02d".format(f.battle.at.hour, f.battle.at.minute),
                SergeantLine.waveText(f.wave.squads, content), (f.winChance * 100).roundToInt())
        }
        return GameUi(
            race = s.race,
            supplies = s.supplies,
            sergeant = SergeantLine.home(s, snap.forecast, content, snap.at),
            forecast = forecast,
            onLeave = onLeave,
            wentToday = today in s.sessions,
            card = HomeCardUi(HomeCards.forDay(today, s.seasonSeed), today in s.homeCards),
            away = snap.unseen.map { SergeantLine.result(it, content) },
            hq = race?.hq?.art ?: ArtId("missing"),
            buildings = race?.buildings.orEmpty().map { b ->
                val at = s.buildings[b.id]
                BuildingUi(b.id, b.name, b.art, at?.level ?: 1, b.maxLevel, at?.occupied == true)
            },
            army = race?.units.orEmpty().mapNotNull { u ->
                val ready = s.ready[u.id] ?: 0
                val wounded = s.wounded[u.id] ?: 0
                if (ready + wounded == 0) null else SquadUi(u.id, u.name, u.art, ready, wounded)
            },
            time = snap.at.format(TIME),
        )
    }

    /** The sheet for [building], with the +/− picks kept (a missing pick is 1). Null if the race has no such building. */
    fun sheet(snap: GameSnapshot, content: GameContent, building: BuildingId, picks: Map<UnitId, Int>): BuildingSheet? {
        val s = snap.state
        val race = content.race(s.race) ?: return null
        val def = race.building(building) ?: return null
        val at = s.buildings[building]
        val level = at?.level ?: 1
        val occupied = at?.occupied == true
        val upgradeCost = if (level >= def.maxLevel) null else def.upgradeCosts.getOrNull(level - 1)
        return BuildingSheet(
            id = def.id,
            name = def.name,
            art = def.art,
            level = level,
            maxLevel = def.maxLevel,
            occupied = occupied,
            upgradeCost = upgradeCost,
            canUpgrade = upgradeCost != null && !occupied && s.supplies >= upgradeCost,
            units = def.units.mapNotNull { id -> race.unit(id) }.map { u ->
                val count = (picks[u.id] ?: 1).coerceIn(1, SheetUnit.MAX)
                val locked = level < u.tier
                SheetUnit(u.id, u.name, u.art, u.cost, u.tier, locked, count, u.cost * count,
                    canTrain = !locked && !occupied && s.supplies >= u.cost * count, ready = s.ready[u.id] ?: 0)
            },
        )
    }

    /** "3 Riflemen ready." */
    fun trained(name: String, count: Int): String = if (count == 1) "1 $name ready." else "$count ${plural(name)} ready."

    /** "Barracks: level 2. Medics unlocked." */
    fun upgraded(sheet: BuildingSheet, content: GameContent): String {
        val next = sheet.level + 1
        val unlocked = sheet.units.firstOrNull { it.tier == next }?.name
        return "${sheet.name}: level $next." + (unlocked?.let { " ${plural(it)} unlocked." } ?: "")
    }

    fun refused(why: Refusal): String = when (why) {
        Refusal.ALREADY_TODAY -> "That's done for today, recruit."
        Refusal.FUTURE -> "Can't log tomorrow today, recruit."
        Refusal.NOT_ENOUGH -> "Not enough supplies. A gym session brings 100."
        Refusal.LOCKED -> "Upgrade the building first."
        Refusal.OCCUPIED -> "Occupied. One gym session and we take it back."
        Refusal.MAX_LEVEL -> "Fully upgraded, recruit."
        Refusal.ALREADY_ON_LEAVE -> "Already on leave."
        Refusal.NOT_ON_LEAVE -> "Not on leave, recruit."
        Refusal.UNKNOWN -> "Can't do that right now."
    }

    const val CARD_DONE = "+10 supplies. Every rep counts."
    const val BACK_FROM_LEAVE = "Welcome back, recruit. The gym's waiting."
    const val STARTED_FRESH = "Couldn't read the old save. I kept it aside and started a new base."

    private fun plural(name: String) = SergeantLine.plural(name)
}
