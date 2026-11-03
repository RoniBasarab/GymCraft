package com.roni.gymcraft.engine.economy

import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.model.BuildingState
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.testing.Seasons
import java.time.DayOfWeek
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EconomyTest {
    private val c = DefaultContent
    private val sun = Seasons.START

    private fun ok(change: Change): Change.Ok = change as? Change.Ok ?: throw AssertionError("refused: $change")
    private fun refused(change: Change): Refusal = (change as? Change.Refused)?.why ?: throw AssertionError("expected a refusal")
    private fun rich(s: GameState = Seasons.newGame()) = s.copy(supplies = 10_000)

    @Test
    fun newGame_startsOnTheNextSunday_buildingsAtLevel1() {
        val wed = sun.plusDays(3)
        val s = Economy.newGame(c, Outpost.id, Brood.id, wed, wed.atTime(10, 0), 7L)
        assertEquals(DayOfWeek.SUNDAY, s.seasonStart.dayOfWeek)
        assertEquals(sun.plusDays(7), s.seasonStart)
        assertEquals(Economy.newGame(c, Outpost.id, Brood.id, sun, sun.atStartOfDay(), 7L).seasonStart, sun)
        assertEquals(mapOf(Outpost.Barracks to BuildingState(1), Outpost.MotorPool to BuildingState(1)), s.buildings)
        assertEquals(Economy.START_SUPPLIES, s.supplies)
    }

    @Test
    fun session_pays100_onceADay_neverInTheFuture() {
        val s = ok(Economy.session(Seasons.newGame(), sun, sun))
        assertEquals(Economy.START_SUPPLIES + 100, s.state.supplies)
        assertEquals(Refusal.ALREADY_TODAY, refused(Economy.session(s.state, sun, sun)))
        assertEquals(Refusal.FUTURE, refused(Economy.session(s.state, sun.plusDays(1), sun)))
        assertFalse(s.rally)
    }

    @Test
    fun rally_afterSevenEmptyDays_leaveDaysDontCount() {
        val first = ok(Economy.session(Seasons.newGame(), sun, sun)).state
        // 6 empty days in between: no rally. 7: rally.
        assertFalse(ok(Economy.session(first, sun.plusDays(7), sun.plusDays(7))).rally)
        val back = ok(Economy.session(first, sun.plusDays(8), sun.plusDays(8)))
        assertTrue(back.rally)
        assertEquals(200, back.gained)
        // 3 of those 7 days on leave: only 4 count, no rally.
        val onLeave = first.copy(leave = listOf(com.roni.gymcraft.engine.model.LeavePeriod(sun.plusDays(2), sun.plusDays(4))))
        assertFalse(ok(Economy.session(onLeave, sun.plusDays(8), sun.plusDays(8))).rally)
    }

    @Test
    fun session_bringsWoundedBack_freesBuildings_endsLeave() {
        val s = Seasons.newGame().copy(
            ready = mapOf(Outpost.Rifleman to 2),
            wounded = mapOf(Outpost.Rifleman to 3, Outpost.Buggy to 1),
            buildings = mapOf(Outpost.Barracks to BuildingState(2, occupied = true), Outpost.MotorPool to BuildingState(1)),
            leave = listOf(com.roni.gymcraft.engine.model.LeavePeriod(sun.minusDays(3))),
        )
        val after = ok(Economy.session(s, sun, sun)).state
        assertEquals(mapOf(Outpost.Rifleman to 5, Outpost.Buggy to 1), after.ready)
        assertTrue(after.wounded.isEmpty())
        assertTrue(after.buildings.values.none { it.occupied })
        assertEquals(sun.minusDays(1), after.leave.single().until)
    }

    @Test
    fun homeCard_10_onceADay() {
        val s = ok(Economy.homeCard(Seasons.newGame(), sun, sun))
        assertEquals(Economy.START_SUPPLIES + 10, s.state.supplies)
        assertEquals(Refusal.ALREADY_TODAY, refused(Economy.homeCard(s.state, sun, sun)))
        assertEquals(Economy.START_SUPPLIES + 20, ok(Economy.homeCard(s.state, sun.plusDays(1), sun.plusDays(1))).state.supplies)
    }

    @Test
    fun train_needsTheTier_theSupplies_andAFreeBuilding() {
        val s = Seasons.newGame()
        assertEquals(50, ok(Economy.train(s, c, Outpost.Rifleman)).state.supplies)
        assertEquals(Refusal.NOT_ENOUGH, refused(Economy.train(s, c, Outpost.Rifleman, 3)))
        assertEquals(Refusal.LOCKED, refused(Economy.train(rich(s), c, Outpost.Tank)))
        val occupied = rich(s).copy(buildings = s.buildings + (Outpost.Barracks to BuildingState(1, occupied = true)))
        assertEquals(Refusal.OCCUPIED, refused(Economy.train(occupied, c, Outpost.Rifleman)))
        assertEquals(Refusal.UNKNOWN, refused(Economy.train(rich(s), c, Brood.Biter)))
        assertEquals(mapOf(Outpost.Rifleman to 4), ok(Economy.train(rich(s), c, Outpost.Rifleman, 4)).state.ready)
    }

    @Test
    fun upgrade_150_then_300_then_max() {
        var s = rich()
        s = ok(Economy.upgrade(s, c, Outpost.Barracks)).state
        assertEquals(10_000 - 150, s.supplies)
        s = ok(Economy.upgrade(s, c, Outpost.Barracks)).state
        assertEquals(10_000 - 450, s.supplies)
        assertEquals(3, s.level(Outpost.Barracks))
        assertEquals(Refusal.MAX_LEVEL, refused(Economy.upgrade(s, c, Outpost.Barracks)))
        assertTrue(Economy.train(s, c, Outpost.Rocketeer) is Change.Ok)
    }

    @Test
    fun leave_startsOnce_andEnds() {
        val s = ok(Economy.startLeave(Seasons.newGame(), sun)).state
        assertTrue(s.onLeave(sun.plusDays(5)))
        assertEquals(Refusal.ALREADY_ON_LEAVE, refused(Economy.startLeave(s, sun.plusDays(1))))
        val back = ok(Economy.endLeave(s, sun.plusDays(4))).state
        assertTrue(back.onLeave(sun.plusDays(3)))
        assertFalse(back.onLeave(sun.plusDays(4)))
        assertEquals(Refusal.NOT_ON_LEAVE, refused(Economy.endLeave(back, sun.plusDays(5))))
        // A leave ended the same day it started leaves nothing behind.
        assertTrue(ok(Economy.endLeave(s, sun)).state.leave.isEmpty())
    }
}
