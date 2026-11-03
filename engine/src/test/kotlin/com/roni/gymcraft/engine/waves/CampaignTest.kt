package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BuildingState
import com.roni.gymcraft.engine.model.LeavePeriod
import com.roni.gymcraft.engine.testing.Seasons
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CampaignTest {
    private val c = DefaultContent
    private val start = Seasons.START

    @Test
    fun aWeek_fightsInOrder_andTwiceChangesNothing() {
        val s = Seasons.newGame().copy(ready = mapOf(Outpost.Rifleman to 6, Outpost.Buggy to 4))
        val now = start.plusDays(6).atTime(21, 0)
        val r = Campaign.resolveDue(s, c, now)
        assertEquals(7, r.battles.size)
        assertEquals(r.battles.map { it.at }.sorted(), r.battles.map { it.at })
        assertEquals(BattleKind.BOSS, r.battles.last().kind)
        assertEquals(now, r.state.resolvedThrough)
        val again = Campaign.resolveDue(r.state, c, now)
        assertTrue(again.battles.isEmpty())
        assertEquals(r.state, again.state)
    }

    @Test
    fun theFallen_areWounded_andASessionBringsThemBack() {
        val s = Seasons.newGame().copy(ready = mapOf(Outpost.Rifleman to 3))
        val r = Campaign.resolveDue(s, c, start.plusDays(6).atTime(21, 0))
        val fallen = r.battles.sumOf { it.fallen.values.sum() }
        assertTrue("3 riflemen against a whole week should lose some", fallen > 0)
        assertEquals(3, (r.state.ready[Outpost.Rifleman] ?: 0) + (r.state.wounded[Outpost.Rifleman] ?: 0))
        val back = (Economy.session(r.state, start.plusDays(7), start.plusDays(7)) as Change.Ok).state
        assertEquals(3, back.ready[Outpost.Rifleman])
    }

    @Test
    fun aLostBoss_occupiesTheHighestBuilding_untilTheNextSession() {
        val s = Seasons.newGame().copy(buildings = mapOf(Outpost.Barracks to BuildingState(1), Outpost.MotorPool to BuildingState(2)))
        val r = Campaign.resolveDue(s, c, start.plusDays(6).atTime(21, 0))
        val boss = r.battles.last()
        assertFalse(boss.won)
        assertEquals(Outpost.MotorPool, boss.occupied)
        assertTrue(r.state.buildings.getValue(Outpost.MotorPool).occupied)
        assertEquals(com.roni.gymcraft.engine.economy.Refusal.OCCUPIED,
            (Economy.train(r.state.copy(supplies = 1000), c, Outpost.Tank) as Change.Refused).why)
        val freed = (Economy.session(r.state, start.plusDays(7), start.plusDays(7)) as Change.Ok).state
        assertFalse(freed.buildings.getValue(Outpost.MotorPool).occupied)
    }

    @Test
    fun onLeave_noBattles_butTimeMovesOn() {
        val s = Seasons.newGame().copy(leave = listOf(LeavePeriod(start)))
        val now = start.plusDays(13).atTime(21, 0)
        val r = Campaign.resolveDue(s, c, now)
        assertTrue(r.battles.isEmpty())
        assertEquals(now, r.state.resolvedThrough)
    }

    @Test
    fun warmUp_noBattlesBeforeTheSeasonStarts() {
        val wed = start.plusDays(3)
        val s = Economy.newGame(c, Outpost.id, Brood.id, wed, wed.atTime(9, 0), 3L)
        val r = Campaign.resolveDue(s, c, wed.plusDays(3).atTime(23, 0))
        assertTrue(r.battles.isEmpty())
        assertEquals(start.plusDays(7), r.state.seasonStart)
    }

    @Test
    fun rewatch_isTheSameBattle() {
        val s = Seasons.newGame().copy(ready = mapOf(Outpost.Rifleman to 6))
        val outcome = Campaign.resolveDue(s, c, start.plusDays(6).atTime(21, 0)).battles.last()
        val replay = com.roni.gymcraft.engine.battle.BattleSim.run(outcome.input, c, record = true)
        assertEquals(outcome.won, replay.won)
        assertEquals(outcome.fallen, replay.squads.filter { it.side == com.roni.gymcraft.engine.battle.Side.DEFENDER && it.fallen > 0 }.associate { it.unit to it.fallen })
    }
}
