package com.roni.gymcraft.feature.home

import com.roni.gymcraft.engine.battle.BattleInput
import com.roni.gymcraft.engine.battle.SquadSpec
import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.content.Traits
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BattleOutcome
import com.roni.gymcraft.engine.model.LeavePeriod
import com.roni.gymcraft.engine.economy.Economy
import java.time.LocalDate
import com.roni.gymcraft.engine.waves.Forecast
import com.roni.gymcraft.engine.waves.ScheduledBattle
import com.roni.gymcraft.engine.waves.Wave
import org.junit.Assert.assertEquals
import org.junit.Test

class SergeantLineTest {
    private val c = DefaultContent
    private val start = LocalDate.of(2026, 11, 1)   // a Sunday
    private fun newGame() = Economy.newGame(c, Outpost.id, Brood.id, start, start.atStartOfDay(), 1L)
    private val saturday = start.plusDays(6).atTime(20, 0)
    private val wave = Wave(listOf(SquadSpec(Brood.Brute, 2), SquadSpec(Brood.Biter, 30)), Traits.Armored)
    private fun forecast(needed: Int?) = Forecast(ScheduledBattle(saturday, BattleKind.BOSS, 1), wave, Outpost.Rocketeer, 0.3, needed)
    private val tuesday = start.plusDays(2).atTime(9, 0)

    @Test
    fun home_byWhatTheForecastNeeds() {
        val s = newGame()
        assertEquals("Coming Saturday: 30 Biters and 2 Brutes. We're ready. Keep it that way.",
            SergeantLine.home(s, forecast(0), c, tuesday))
        assertEquals("One more session by Saturday, recruit, or that wall falls. Armored wave: Rocketeers crack it.",
            SergeantLine.home(s, forecast(1), c, tuesday))
        assertEquals("2 more sessions by Saturday, recruit, or that wall falls. Armored wave: Rocketeers crack it.",
            SergeantLine.home(s, forecast(2), c, tuesday))
        assertEquals("Coming Saturday: 30 Biters and 2 Brutes. Too big for us yet. Every session still counts. Armored wave: Rocketeers crack it.",
            SergeantLine.home(s, forecast(null), c, tuesday))
        assertEquals("One more session tonight, recruit, or that wall falls. Armored wave: Rocketeers crack it.",
            SergeantLine.home(s, forecast(1), c, start.plusDays(6).atTime(10, 0)))
    }

    @Test
    fun home_onLeave_warmUp_seasonOver() {
        val s = newGame()
        assertEquals("On leave. Get well. The base will wait.",
            SergeantLine.home(s.copy(leave = listOf(LeavePeriod(start))), forecast(1), c, tuesday))
        assertEquals("Season starts Sunday, recruit. Every session now is a head start.",
            SergeantLine.home(s.copy(seasonStart = start.plusDays(7)), forecast(1), c, tuesday))
        assertEquals("Season's over, recruit. Stand easy.", SergeantLine.home(s, null, c, tuesday))
    }

    @Test
    fun session_andRally() {
        val s = newGame()
        assertEquals("Good work, recruit. +100 supplies.", SergeantLine.session(Change.Ok(s, 100, rally = false)))
        assertEquals("You're back. The troops noticed. Rally bonus: +100. +200 supplies.", SergeantLine.session(Change.Ok(s, 200, rally = true)))
    }

    @Test
    fun results_heldLostOccupied() {
        val input = BattleInput(Outpost.id, emptyList(), Brood.id, emptyList(), 1L)
        val tue = start.plusDays(2).atTime(20, 0)
        assertEquals("Raid Tue: held.", SergeantLine.result(BattleOutcome(tue, BattleKind.RAID, 1, true, input, emptyMap(), null), c))
        assertEquals("Raid Tue: held. 2 wounded.", SergeantLine.result(BattleOutcome(tue, BattleKind.RAID, 1, true, input, mapOf(Outpost.Rifleman to 2), null), c))
        assertEquals("Boss Sat: lost. The Motor Pool fell. 5 wounded. One gym session and we take it back.",
            SergeantLine.result(BattleOutcome(saturday, BattleKind.BOSS, 1, false, input, mapOf(Outpost.Tank to 5), Outpost.MotorPool), c))
    }

    @Test
    fun plurals() {
        assertEquals(listOf("Biters", "Riflemen", "Rocketeers", "Brutes"), listOf("Biter", "Rifleman", "Rocketeer", "Brute").map(SergeantLine::plural))
    }

    @Test
    fun waveText_pluralsAndOrder() {
        assertEquals("1 Brute", SergeantLine.waveText(listOf(SquadSpec(Brood.Brute, 1)), c))
        assertEquals("12 Spitters, 9 Biters and 1 Brute",
            SergeantLine.waveText(listOf(SquadSpec(Brood.Brute, 1), SquadSpec(Brood.Biter, 9), SquadSpec(Brood.Spitter, 12)), c))
        assertEquals("nothing", SergeantLine.waveText(emptyList(), c))
    }
}
