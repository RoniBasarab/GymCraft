package com.roni.gymcraft.engine.testing

import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.waves.Campaign
import com.roni.gymcraft.engine.waves.Planner
import com.roni.gymcraft.engine.waves.Schedule
import com.roni.gymcraft.engine.waves.Waves
import java.time.DayOfWeek
import java.time.LocalDate

/** Test helpers: a season played by the reference player at a steady pace. */
object Seasons {
    /** A Sunday, so the season starts at once. */
    val START: LocalDate = LocalDate.of(2026, 11, 1)

    fun newGame(seed: Long = 1L): GameState =
        Economy.newGame(DefaultContent, Outpost.id, Brood.id, START, START.atStartOfDay(), seed)

    fun sessionDays(pace: Int): Set<DayOfWeek> = when (pace) {
        3 -> setOf(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        2 -> setOf(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY)
        1 -> setOf(DayOfWeek.TUESDAY)
        else -> emptySet()
    }

    /** Plays 12 weeks: [pace] sessions a week, none in the weeks in [off]. Returns each week's boss result. */
    fun play(pace: Int, seed: Long, off: Set<Int> = emptySet()): BooleanArray {
        val c = DefaultContent
        var s = newGame(seed)
        val boss = BooleanArray(Schedule.WEEKS)
        for (d in 0 until Schedule.WEEKS * 7) {
            val day = START.plusDays(d.toLong())
            if ((d / 7 + 1) !in off && day.dayOfWeek in sessionDays(pace)) {
                s = (Economy.session(s, day, day) as Change.Ok).state
                val trait = Schedule.nextBoss(s, day.atStartOfDay())?.let { Waves.wave(s, c, it).mainTrait }
                s = Planner.spend(s, c, trait)
            }
            val r = Campaign.resolveDue(s, c, day.atTime(23, 59))
            s = r.state
            r.battles.filter { it.kind == BattleKind.BOSS }.forEach { boss[it.week - 1] = it.won }
        }
        return boss
    }

    /** Share of bosses won over [seeds] seasons, for the weeks in [weeks] (1-based). */
    fun winRate(pace: Int, seeds: Int = 40, off: Set<Int> = emptySet(), weeks: IntRange = 1..Schedule.WEEKS): Double {
        var wins = 0; var total = 0
        for (seed in 1..seeds) {
            val b = play(pace, seed.toLong(), off)
            for (w in weeks) { total++; if (b[w - 1]) wins++ }
        }
        return wins.toDouble() / total
    }
}
