package com.roni.gymcraft.feature.home

import com.roni.gymcraft.core.game.GameSnapshot
import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.BuildingState
import com.roni.gymcraft.engine.model.LeavePeriod
import com.roni.gymcraft.engine.waves.Campaign
import com.roni.gymcraft.engine.waves.Forecaster
import java.time.LocalDate
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUiTest {
    private val c = DefaultContent
    private val start = LocalDate.of(2026, 11, 1)   // a Sunday
    private val tuesday = LocalDateTime.of(2026, 11, 3, 9, 0)
    private fun newGame() = Economy.newGame(c, Outpost.id, Brood.id, start, start.atStartOfDay(), 1L)
    private fun snap(state: com.roni.gymcraft.engine.model.GameState, at: LocalDateTime = tuesday, seen: Int = 0) =
        GameSnapshot(state, seen, Forecaster.nextBoss(state, c, at), at)

    @Test
    fun aNewGame_showsTheBaseAndTheForecast() {
        val g = HomeUi.game(snap(newGame()), c)
        assertEquals(100, g.supplies)
        assertEquals(listOf("Barracks", "Motor Pool"), g.buildings.map { it.name })
        assertTrue(g.army.isEmpty())
        assertFalse(g.wentToday)
        assertEquals("Boss · Sat 20:00", g.forecast?.title)
        assertEquals(Outpost.race.hq.art, g.hq)
        assertEquals("Tue 3 Nov 09:00", g.time)
        assertTrue(g.card.text in HomeCards.all && !g.card.done)
    }

    @Test
    fun theArmy_listsReadyAndWounded_inTheRacesOrder() {
        val s = newGame().copy(ready = mapOf(Outpost.Tank to 2, Outpost.Rifleman to 5), wounded = mapOf(Outpost.Rifleman to 1, Outpost.Buggy to 3))
        assertEquals(listOf(SquadUi(Outpost.Rifleman, "Rifleman", Outpost.race.unit(Outpost.Rifleman)?.art ?: throw AssertionError(), 5, 1)),
            HomeUi.game(snap(s), c).army.take(1))
        assertEquals(listOf("Rifleman", "Buggy", "Tank"), HomeUi.game(snap(s), c).army.map { it.name })
    }

    @Test
    fun onLeave_hidesTheForecast() {
        val g = HomeUi.game(snap(newGame().copy(leave = listOf(LeavePeriod(start)))), c)
        assertTrue(g.onLeave)
        assertNull(g.forecast)
        assertEquals("On leave. Get well. The base will wait.", g.sergeant)
    }

    @Test
    fun theAwayCard_listsUnseenBattles() {
        val fought = Campaign.resolveDue(newGame(), c, LocalDateTime.of(2026, 11, 3, 21, 0)).state
        assertEquals(listOf("Raid Sun:", "Raid Mon:", "Raid Tue:"), HomeUi.game(snap(fought, seen = 0), c).away.map { it.take(9) })
        assertEquals(1, HomeUi.game(snap(fought, seen = 2), c).away.size)
    }

    @Test
    fun theSheet_locksByLevel_andPricesByCount() {
        val s = newGame().copy(supplies = 160)
        val sheet = HomeUi.sheet(snap(s), c, Outpost.Barracks, mapOf(Outpost.Rifleman to 3)) ?: throw AssertionError()
        assertEquals(listOf(false, true, true), sheet.units.map { it.locked })
        val rifle = sheet.units[0]
        assertEquals(3, rifle.count); assertEquals(150, rifle.total); assertTrue(rifle.canTrain)
        assertFalse(HomeUi.sheet(snap(s), c, Outpost.Barracks, mapOf(Outpost.Rifleman to 4))?.units?.get(0)?.canTrain ?: true)
        assertEquals(150, sheet.upgradeCost); assertTrue(sheet.canUpgrade)
        assertEquals(SheetUnit.MAX, HomeUi.sheet(snap(s), c, Outpost.Barracks, mapOf(Outpost.Rifleman to 99))?.units?.get(0)?.count)
        assertEquals(1, HomeUi.sheet(snap(s), c, Outpost.Barracks, mapOf(Outpost.Rifleman to 0))?.units?.get(0)?.count)
    }

    @Test
    fun theSheet_occupiedAndMaxLevel() {
        val s = newGame().copy(supplies = 1000, buildings = mapOf(Outpost.Barracks to BuildingState(3, occupied = true), Outpost.MotorPool to BuildingState(1)))
        val sheet = HomeUi.sheet(snap(s), c, Outpost.Barracks, emptyMap()) ?: throw AssertionError()
        assertNull(sheet.upgradeCost); assertFalse(sheet.canUpgrade)
        assertTrue(sheet.units.none { it.locked } && sheet.units.none { it.canTrain })
        assertTrue(HomeUi.game(snap(s), c).buildings[0].occupied)
    }

    @Test
    fun texts() {
        assertEquals("3 Riflemen ready.", HomeUi.trained("Rifleman", 3))
        assertEquals("1 Tank ready.", HomeUi.trained("Tank", 1))
        val sheet = HomeUi.sheet(snap(newGame()), c, Outpost.Barracks, emptyMap()) ?: throw AssertionError()
        assertEquals("Barracks: level 2. Medics unlocked.", HomeUi.upgraded(sheet, c))
    }

    @Test
    fun theHomeCard_isFixedForTheDay_andVaries() {
        assertEquals(HomeCards.forDay(start, 1L), HomeCards.forDay(start, 1L))
        val week = (0L until 14L).map { HomeCards.forDay(start.plusDays(it), 1L) }.toSet()
        assertTrue("a fortnight should show several cards: $week", week.size >= 5)
        val after = Economy.homeCard(newGame(), tuesday.toLocalDate(), tuesday.toLocalDate()) as Change.Ok
        assertTrue(HomeUi.game(snap(after.state), c).card.done)
    }
}
