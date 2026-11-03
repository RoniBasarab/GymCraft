package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.testing.Seasons
import java.time.DayOfWeek
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WavesTest {
    private val c = DefaultContent
    private val start = Seasons.START

    @Test
    fun aSeason_is12Weeks_raidsSundayToFriday_bossSaturdayAt20() {
        val all = Schedule.battles(start).toList()
        assertEquals(84, all.size)
        val bosses = all.filter { it.kind == BattleKind.BOSS }
        assertEquals(12, bosses.size)
        assertTrue(bosses.all { it.at.dayOfWeek == DayOfWeek.SATURDAY && it.at.toLocalTime() == LocalTime.of(20, 0) })
        assertEquals((1..12).toList(), bosses.map { it.week })
        assertEquals(1, Schedule.weekOf(start, start.plusDays(6)))
        assertEquals(2, Schedule.weekOf(start, start.plusDays(7)))
        assertEquals(0, Schedule.weekOf(start, start.minusDays(1)))
    }

    @Test
    fun budgets_grow_andTheFinalBossIsBigger() {
        assertTrue(Waves.budget(BattleKind.BOSS, 6) > Waves.budget(BattleKind.BOSS, 2))
        assertTrue(Waves.budget(BattleKind.RAID, 6) < Waves.budget(BattleKind.BOSS, 6))
        val expected12 = (Waves.BOSS_BASE + Waves.BOSS_PER_WEEK * 11) * Waves.FINAL_BOSS
        assertEquals(expected12, Waves.budget(BattleKind.BOSS, 12), 1e-9)
    }

    @Test
    fun aWave_isDeterministic_spendsItsBudget_andHasNoTier3InWeek1() {
        val s = Seasons.newGame(seed = 9L)
        val battles = Schedule.battles(start).toList()
        for (b in battles) {
            val w = Waves.wave(s, c, b)
            assertEquals(w, Waves.wave(s, c, b))
            val spent = w.squads.sumOf { (c.unit(it.unit)?.cost ?: 0) * it.count }
            val budget = Waves.budget(b.kind, b.week)
            assertTrue("spent $spent of $budget", spent <= budget + 1e-9 && spent > budget - 15.0 - 1e-9)
            if (b.week == 1) assertTrue(w.squads.none { it.unit == Brood.Brute })
        }
        // Different seasons, different waves (the theme draw).
        val other = Seasons.newGame(seed = 10L)
        assertTrue(battles.any { Waves.wave(s, c, it) != Waves.wave(other, c, it) })
    }

    @Test
    fun due_skipsLeaveDays_andWhatWasResolved() {
        val s = Seasons.newGame().copy(leave = listOf(com.roni.gymcraft.engine.model.LeavePeriod(start.plusDays(1), start.plusDays(2))))
        val due = Schedule.due(s, start.plusDays(6).atTime(21, 0))
        assertEquals(listOf(0L, 3L, 4L, 5L, 6L), due.map { java.time.temporal.ChronoUnit.DAYS.between(start, it.at.toLocalDate()) })
        val later = s.copy(resolvedThrough = start.plusDays(4).atTime(20, 0))
        assertEquals(2, Schedule.due(later, start.plusDays(6).atTime(21, 0)).size)
    }
}
