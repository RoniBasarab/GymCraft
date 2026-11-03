package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.content.Traits
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BuildingState
import com.roni.gymcraft.engine.testing.Seasons
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ForecastTest {
    private val c = DefaultContent
    private val start = Seasons.START

    @Test
    fun theForecast_isTheNextBoss_andDeterministic() {
        val s = Seasons.newGame()
        val f = Forecaster.nextBoss(s, c, start.atTime(9, 0))
        assertNotNull(f)
        f ?: return
        assertEquals(BattleKind.BOSS, f.battle.kind)
        assertEquals(start.plusDays(6).atTime(20, 0), f.battle.at)
        assertEquals(f, Forecaster.nextBoss(s, c, start.atTime(9, 0)))
    }

    @Test
    fun aStrongArmy_needsNothingMore_anEmptyOneNeedsSessions() {
        val maxed = Seasons.newGame().copy(
            buildings = mapOf(Outpost.Barracks to BuildingState(3), Outpost.MotorPool to BuildingState(3)),
            ready = mapOf(Outpost.Rifleman to 40, Outpost.Tank to 20, Outpost.Rocketeer to 10, Outpost.Walker to 6),
        )
        val strong = Forecaster.nextBoss(maxed, c, start.atTime(9, 0))
        assertEquals(0, strong?.sessionsNeeded)
        // Week 3's boss against an empty base: some sessions, or more than the forecast looks ahead.
        val empty = Seasons.newGame()
        val weak = Forecaster.nextBoss(empty, c, start.plusDays(14).atTime(9, 0))
        assertNotNull(weak)
        assertTrue(weak?.winChance == 0.0)
        assertTrue(weak?.sessionsNeeded.let { it == null || it > 0 })
    }

    @Test
    fun counters_rocketeerForArmor_tankForSwarms() {
        val s = Seasons.newGame()
        assertEquals(Outpost.Rocketeer, Planner.counter(s, c, Traits.Armored, unlockedOnly = false))
        assertEquals(Outpost.Tank, Planner.counter(s, c, Traits.Swarm, unlockedOnly = false))
        // Unlocked only, at level 1: no Rocketeer yet.
        assertTrue(Planner.counter(s, c, Traits.Armored, unlockedOnly = true) in setOf(Outpost.Rifleman, Outpost.Buggy))
    }

    @Test
    fun thePlanner_spendsWhatItCan_andKeepsArmiesMixed() {
        val s = Planner.spend(Seasons.newGame().copy(supplies = 500), c, null)
        assertTrue(s.supplies < 50)
        assertTrue("mixed: ${s.ready}", s.ready.size >= 2)
    }
}
