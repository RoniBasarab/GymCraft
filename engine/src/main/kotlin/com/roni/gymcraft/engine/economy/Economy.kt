package com.roni.gymcraft.engine.economy

import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.BuildingState
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.model.LeavePeriod
import com.roni.gymcraft.engine.model.RaceId
import com.roni.gymcraft.engine.model.UnitId
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

/** Why a rule said no. The app turns each into the drill sergeant's line. */
enum class Refusal { ALREADY_TODAY, FUTURE, NOT_ENOUGH, LOCKED, OCCUPIED, MAX_LEVEL, UNKNOWN, NOT_ON_LEAVE, ALREADY_ON_LEAVE }

sealed interface Change {
    data class Ok(val state: GameState, val gained: Int = 0, val rally: Boolean = false) : Change
    data class Refused(val why: Refusal) : Change
}

/**
 * Supplies in and out (game-design.md §3, §5). Pure: every function takes a state and gives a new one.
 * Talks to: [GameContent] for costs and unlocks.
 */
object Economy {
    const val SESSION = 100
    const val RALLY = 100
    const val RALLY_AFTER_DAYS = 7
    const val HOME_CARD = 10
    const val START_SUPPLIES = 100

    /**
     * A new game: every building at level 1, [START_SUPPLIES] to begin with, nothing before [now] is fought. The season
     * starts on [today] when it is a Sunday, else on the next Sunday (weeks run Sunday to Saturday, the boss is Saturday):
     * the days before are a warm-up with no battles, while sessions already count, so a first boss never comes after
     * only a day or two.
     */
    fun newGame(content: GameContent, race: RaceId, enemy: RaceId, today: LocalDate, now: LocalDateTime, seed: Long): GameState {
        val def = content.race(race)
        return GameState(
            race = race,
            enemy = enemy,
            seasonStart = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)),
            seasonSeed = seed,
            supplies = START_SUPPLIES,
            buildings = def?.buildings?.associate { it.id to BuildingState() } ?: emptyMap(),
            resolvedThrough = now,
        )
    }

    /**
     * "I went" on [day] (today; never the future, at most one a day). +100, and +100 more when at least
     * [RALLY_AFTER_DAYS] whole days passed without a session (days on leave don't count; a first session ever is no
     * comeback). It brings every
     * wounded unit back, frees every occupied building, and ends a leave that is still on.
     */
    fun session(state: GameState, day: LocalDate, today: LocalDate): Change {
        if (day.isAfter(today)) return Change.Refused(Refusal.FUTURE)
        if (day in state.sessions) return Change.Refused(Refusal.ALREADY_TODAY)
        val last = state.sessions.filter { it.isBefore(day) }.maxOrNull()
        val away = last?.let { daysAway(state, it, day) } ?: 0
        val rally = last != null && away >= RALLY_AFTER_DAYS
        val gained = SESSION + if (rally) RALLY else 0
        val leave = state.leave.map { if (it.until == null && !day.isBefore(it.from)) it.copy(until = day.minusDays(1)) else it }
            .filter { it.until == null || !it.until.isBefore(it.from) }
        val next = state.copy(
            supplies = state.supplies + gained,
            sessions = (state.sessions + day).sorted(),
            ready = merge(state.ready, state.wounded),
            wounded = emptyMap(),
            buildings = state.buildings.mapValues { (_, b) -> b.copy(occupied = false) },
            leave = leave,
        )
        return Change.Ok(next, gained, rally)
    }

    /** A home exercise card done on [day]: +10, at most one a day. */
    fun homeCard(state: GameState, day: LocalDate, today: LocalDate): Change {
        if (day.isAfter(today)) return Change.Refused(Refusal.FUTURE)
        if (day in state.homeCards) return Change.Refused(Refusal.ALREADY_TODAY)
        return Change.Ok(state.copy(supplies = state.supplies + HOME_CARD, homeCards = (state.homeCards + day).sorted()), HOME_CARD)
    }

    /** Trains [count] units of [unit]: its building must be at the unit's tier and not occupied. */
    fun train(state: GameState, content: GameContent, unit: UnitId, count: Int = 1): Change {
        val race = content.race(state.race) ?: return Change.Refused(Refusal.UNKNOWN)
        val def = race.unit(unit) ?: return Change.Refused(Refusal.UNKNOWN)
        val building = race.buildingOf(unit) ?: return Change.Refused(Refusal.UNKNOWN)
        val at = state.buildings[building.id] ?: return Change.Refused(Refusal.UNKNOWN)
        if (count <= 0) return Change.Refused(Refusal.UNKNOWN)
        if (at.occupied) return Change.Refused(Refusal.OCCUPIED)
        if (at.level < def.tier) return Change.Refused(Refusal.LOCKED)
        val cost = def.cost * count
        if (state.supplies < cost) return Change.Refused(Refusal.NOT_ENOUGH)
        return Change.Ok(state.copy(supplies = state.supplies - cost, ready = merge(state.ready, mapOf(unit to count))))
    }

    /** Raises [building] one level for its next upgrade cost. */
    fun upgrade(state: GameState, content: GameContent, building: BuildingId): Change {
        val def = content.race(state.race)?.building(building) ?: return Change.Refused(Refusal.UNKNOWN)
        val at = state.buildings[building] ?: return Change.Refused(Refusal.UNKNOWN)
        if (at.occupied) return Change.Refused(Refusal.OCCUPIED)
        if (at.level >= def.maxLevel) return Change.Refused(Refusal.MAX_LEVEL)
        val cost = def.upgradeCosts.getOrNull(at.level - 1) ?: return Change.Refused(Refusal.MAX_LEVEL)
        if (state.supplies < cost) return Change.Refused(Refusal.NOT_ENOUGH)
        return Change.Ok(state.copy(
            supplies = state.supplies - cost,
            buildings = state.buildings + (building to at.copy(level = at.level + 1)),
        ))
    }

    /** "On leave" from [day]: no battles until it ends (a session or [endLeave]). */
    fun startLeave(state: GameState, day: LocalDate): Change {
        if (state.leave.any { it.until == null }) return Change.Refused(Refusal.ALREADY_ON_LEAVE)
        return Change.Ok(state.copy(leave = state.leave + LeavePeriod(day)))
    }

    /** Back from leave: the leave ends the day before [day] (a leave started today simply disappears). */
    fun endLeave(state: GameState, day: LocalDate): Change {
        if (state.leave.none { it.until == null }) return Change.Refused(Refusal.NOT_ON_LEAVE)
        val leave = state.leave.map { if (it.until == null) it.copy(until = day.minusDays(1)) else it }
            .filter { it.until == null || !it.until.isBefore(it.from) }
        return Change.Ok(state.copy(leave = leave))
    }

    /** Whole days strictly between two sessions that count as away: the days in between minus the days on leave. */
    fun daysAway(state: GameState, from: LocalDate, to: LocalDate): Int {
        val gap = ChronoUnit.DAYS.between(from, to).toInt() - 1
        var leaveDays = 0
        var d = from.plusDays(1)
        while (d.isBefore(to)) { if (state.onLeave(d)) leaveDays++; d = d.plusDays(1) }
        return gap - leaveDays
    }

    fun <K> merge(a: Map<K, Int>, b: Map<K, Int>): Map<K, Int> {
        if (b.isEmpty()) return a
        val out = a.toMutableMap()
        b.forEach { (k, v) -> if (v > 0) out[k] = (out[k] ?: 0) + v }
        return out
    }
}
