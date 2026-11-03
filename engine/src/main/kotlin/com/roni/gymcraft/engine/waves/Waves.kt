package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.battle.SquadSpec
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.model.TraitId
import com.roni.gymcraft.engine.model.UnitDef
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random

/** One battle on the calendar (game-design.md §4). */
data class ScheduledBattle(val at: LocalDateTime, val kind: BattleKind, val week: Int)

/**
 * The season's calendar: weeks run Sunday to Saturday from the season start; a raid every evening Sunday to Friday at
 * [HOUR]:00, the boss on Saturday at [HOUR]:00; [WEEKS] weeks. Days on leave have no battle.
 */
object Schedule {
    const val WEEKS = 12
    const val HOUR = 20

    /** 1-based week of [day]; 0 before the season, above [WEEKS] after it. */
    fun weekOf(seasonStart: LocalDate, day: LocalDate): Int {
        val days = ChronoUnit.DAYS.between(seasonStart, day)
        return if (days < 0) 0 else (days / 7 + 1).toInt()
    }

    fun battles(seasonStart: LocalDate): Sequence<ScheduledBattle> = (0 until WEEKS * 7).asSequence().map { i ->
        val day = seasonStart.plusDays(i.toLong())
        val kind = if (day.dayOfWeek == DayOfWeek.SATURDAY) BattleKind.BOSS else BattleKind.RAID
        ScheduledBattle(day.atTime(LocalTime.of(HOUR, 0)), kind, i / 7 + 1)
    }

    /** Battles after [GameState.resolvedThrough] and up to [now], oldest first, skipping days on leave. */
    fun due(state: GameState, now: LocalDateTime): List<ScheduledBattle> =
        battles(state.seasonStart)
            .filter { it.at.isAfter(state.resolvedThrough) && !it.at.isAfter(now) && !state.onLeave(it.at.toLocalDate()) }
            .toList()

    /** The next boss after [now] (a boss on a leave day still shows: the leave may end first). */
    fun nextBoss(state: GameState, now: LocalDateTime): ScheduledBattle? =
        battles(state.seasonStart).firstOrNull { it.kind == BattleKind.BOSS && it.at.isAfter(now) }
}

/** A wave and the trait most of it carries (what the forecast warns about). */
data class Wave(val squads: List<SquadSpec>, val mainTrait: TraitId)

/**
 * The attackers (game-design.md §4): a budget in enemy cost points that grows every week, spent on the enemy race's
 * units. Each battle draws one main trait: [MAIN_SHARE] of the budget goes to the units carrying it, the rest to the
 * others. Units of tier 3 join from week 2. Deterministic from the season seed, the week and the day.
 *
 * The budget numbers are tuned by `BalanceTest` so that a steady 3 sessions a week wins the boss comfortably,
 * 2 a week is about a coin flip and 1 a week loses (game-design.md §4).
 */
object Waves {
    const val BOSS_BASE = 240.0
    const val BOSS_PER_WEEK = 265.0
    const val FINAL_BOSS = 1.3
    const val RAID_SHARE = 0.30
    const val MAIN_SHARE = 0.65

    fun budget(kind: BattleKind, week: Int): Double {
        val boss = (BOSS_BASE + BOSS_PER_WEEK * (week - 1)) * if (week == Schedule.WEEKS) FINAL_BOSS else 1.0
        return if (kind == BattleKind.BOSS) boss else boss * RAID_SHARE
    }

    fun seed(seasonSeed: Long, battle: ScheduledBattle): Long =
        seasonSeed * 1_000_003L + battle.at.toLocalDate().toEpochDay() * 31L + battle.kind.ordinal

    fun wave(state: GameState, content: GameContent, battle: ScheduledBattle): Wave {
        val units = content.race(state.enemy)?.units.orEmpty()
            .filter { it.tier < 3 || battle.week >= 2 }
            .filter { it.cost > 0 }
        if (units.isEmpty()) return Wave(emptyList(), TraitId(""))
        val rng = Random(seed(state.seasonSeed, battle))
        val traits = units.map { it.trait }.distinct()
        val main = traits[rng.nextInt(traits.size)]
        val budget = budget(battle.kind, battle.week)
        val mainUnits = units.filter { it.trait == main }
        val others = units.filter { it.trait != main }
        val counts = LinkedHashMap<UnitDef, Int>()
        spend(mainUnits, if (others.isEmpty()) budget else budget * MAIN_SHARE, counts)
        spend(others, budget - counts.entries.sumOf { it.key.cost * it.value }, counts)
        // Whatever is left buys the cheapest unit of all, so a wave always uses its budget (to within one unit).
        val cheapest = units.minBy { it.cost }
        val left = budget - counts.entries.sumOf { it.key.cost * it.value }
        if (left >= cheapest.cost) counts[cheapest] = (counts[cheapest] ?: 0) + (left / cheapest.cost).toInt()
        return Wave(counts.filter { it.value > 0 }.map { (u, c) -> SquadSpec(u.id, c) }, main)
    }

    /** Splits [budget] evenly across [units], whole units only; leftovers buy the cheapest. */
    private fun spend(units: List<UnitDef>, budget: Double, into: MutableMap<UnitDef, Int>) {
        if (units.isEmpty() || budget <= 0.0) return
        val share = budget / units.size
        var left = budget
        for (u in units) {
            val c = (share / u.cost).toInt()
            if (c > 0) { into[u] = (into[u] ?: 0) + c; left -= c * u.cost }
        }
        val cheapest = units.minBy { it.cost }
        val extra = (left / cheapest.cost).toInt()
        if (extra > 0) into[cheapest] = (into[cheapest] ?: 0) + extra
    }
}
