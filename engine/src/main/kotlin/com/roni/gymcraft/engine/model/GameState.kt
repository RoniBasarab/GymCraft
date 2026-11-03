package com.roni.gymcraft.engine.model

import com.roni.gymcraft.engine.battle.BattleInput
import java.time.LocalDate
import java.time.LocalDateTime

/** A production building's level (1..its maxLevel) and whether a lost boss occupies it (game-design.md §5). */
data class BuildingState(val level: Int = 1, val occupied: Boolean = false)

/** "On leave" from [from] to [until] inclusive; [until] null = still on leave. */
data class LeavePeriod(val from: LocalDate, val until: LocalDate? = null) {
    fun covers(day: LocalDate): Boolean = !day.isBefore(from) && (until == null || !day.isAfter(until))
}

enum class BattleKind { RAID, BOSS }

/** A battle that happened: its input (Rewatch runs it again), when, and how it went. */
data class BattleOutcome(
    val at: LocalDateTime,
    val kind: BattleKind,
    val week: Int,
    val won: Boolean,
    val input: BattleInput,
    /** Units that fell, now wounded. */
    val fallen: Map<UnitId, Int>,
    /** The building a lost boss occupied, if any. */
    val occupied: BuildingId?,
)

/**
 * The whole game (architecture.md §7 saves exactly this). Immutable: every rule returns a new state.
 * [ready] fights; [wounded] are back after the next gym session. [resolvedThrough]: battles up to here are done.
 */
data class GameState(
    val race: RaceId,
    val enemy: RaceId,
    val seasonStart: LocalDate,
    val seasonSeed: Long,
    val supplies: Int,
    val buildings: Map<BuildingId, BuildingState>,
    val ready: Map<UnitId, Int> = emptyMap(),
    val wounded: Map<UnitId, Int> = emptyMap(),
    val sessions: List<LocalDate> = emptyList(),
    val homeCards: List<LocalDate> = emptyList(),
    val leave: List<LeavePeriod> = emptyList(),
    val resolvedThrough: LocalDateTime,
    val battles: List<BattleOutcome> = emptyList(),
) {
    fun onLeave(day: LocalDate): Boolean = leave.any { it.covers(day) }
    fun level(building: BuildingId): Int = buildings[building]?.level ?: 0
}
