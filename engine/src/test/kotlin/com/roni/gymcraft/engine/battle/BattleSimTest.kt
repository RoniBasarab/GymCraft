package com.roni.gymcraft.engine.battle

import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BattleSimTest {
    private val c = DefaultContent
    private fun input(defenders: List<SquadSpec>, attackers: List<SquadSpec>, seed: Long = 1L) =
        BattleInput(Outpost.id, defenders, Brood.id, attackers, seed)
    private fun winRate(defenders: List<SquadSpec>, attackers: List<SquadSpec>, seeds: Int = 20): Double =
        (1..seeds).count { BattleSim.run(input(defenders, attackers, it.toLong()), c).won }.toDouble() / seeds

    private val armoredWave = listOf(SquadSpec(Brood.Brute, 11), SquadSpec(Brood.Biter, 16))
    private val swarmWave = listOf(SquadSpec(Brood.Biter, 80), SquadSpec(Brood.Spitter, 10))

    @Test
    fun sameInput_sameBattle_rewatchIsExact() {
        val i = input(listOf(SquadSpec(Outpost.Rifleman, 8), SquadSpec(Outpost.Tank, 3, level = 2)), swarmWave, seed = 42L)
        val a = BattleSim.run(i, c, record = true)
        val b = BattleSim.run(i, c, record = true)
        assertEquals(a.won, b.won)
        assertEquals(a.ticks, b.ticks)
        assertEquals(a.squads, b.squads)
        val ra = assertNotNullRecord(a); val rb = assertNotNullRecord(b)
        assertEquals(ra.frames.size, rb.frames.size)
        for (f in ra.frames.indices) {
            assertTrue(ra.frames[f].x.contentEquals(rb.frames[f].x))
            assertTrue(ra.frames[f].count.contentEquals(rb.frames[f].count))
        }
    }

    private fun assertNotNullRecord(r: BattleResult): BattleRecord { assertNotNull(r.record); return r.record ?: throw AssertionError() }

    @Test
    fun theRecord_endsWhereTheResultDoes() {
        val r = BattleSim.run(input(listOf(SquadSpec(Outpost.Rifleman, 10)), swarmWave), c, record = true)
        val record = assertNotNullRecord(r)
        assertEquals(r.ticks, record.frames.size)
        assertEquals(r.squads.map { it.left }, record.frames.last().count.toList())
        assertEquals(r.hqHpLeft, record.frames.last().hqHp.toDouble(), 0.01)
    }

    @Test
    fun noAttackers_isAWinAtOnce() {
        val r = BattleSim.run(input(listOf(SquadSpec(Outpost.Rifleman, 2)), emptyList()), c)
        assertTrue(r.won)
        assertEquals(0, r.ticks)
    }

    @Test
    fun anEmptyBase_losesToARealWave() {
        val r = BattleSim.run(input(emptyList(), swarmWave), c)
        assertFalse(r.won)
        assertEquals(0.0, r.hqHpLeft, 1e-9)
    }

    @Test
    fun counters_rocketsCrackArmor_tanksShredSwarms() {
        // Same supplies (800) each, against waves twice as strong as an even fight.
        assertTrue(winRate(listOf(SquadSpec(Outpost.Rocketeer, 4)), armoredWave) >= 0.8)
        assertTrue(winRate(listOf(SquadSpec(Outpost.Rifleman, 16)), armoredWave) <= 0.2)
        assertTrue(winRate(listOf(SquadSpec(Outpost.Tank, 8)), swarmWave) >= 0.5)
        assertTrue(winRate(listOf(SquadSpec(Outpost.Rifleman, 16)), swarmWave) <= 0.2)
    }

    @Test
    fun rocketsAreWastedOnSwarms_noOverkill() {
        val swarm = listOf(SquadSpec(Brood.Biter, 45), SquadSpec(Brood.Spitter, 6))
        assertTrue(winRate(listOf(SquadSpec(Outpost.Rocketeer, 4)), swarm) <= 0.2)
        assertTrue(winRate(listOf(SquadSpec(Outpost.Rifleman, 16)), swarm) >= 0.8)
    }

    @Test
    fun medics_healInfantry() {
        val r = BattleSim.run(input(listOf(SquadSpec(Outpost.Rifleman, 10), SquadSpec(Outpost.Medic, 2, level = 2)), swarmWave), c, record = true)
        val heals = assertNotNullRecord(r).frames.sumOf { f -> f.events.count { it.kind == BattleEvent.Kind.HEAL } }
        assertTrue("expected heals, got $heals", heals > 0)
    }

    @Test
    fun levels_makeTheSameArmyStronger() {
        val wave = listOf(SquadSpec(Brood.Biter, 40), SquadSpec(Brood.Spitter, 8))
        val l1 = winRate(listOf(SquadSpec(Outpost.Rifleman, 12, level = 1)), wave)
        val l3 = winRate(listOf(SquadSpec(Outpost.Rifleman, 12, level = 3)), wave)
        assertTrue("level 1 $l1, level 3 $l3", l3 >= l1)
    }
}
