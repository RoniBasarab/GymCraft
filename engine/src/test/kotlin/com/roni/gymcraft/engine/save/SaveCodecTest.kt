package com.roni.gymcraft.engine.save

import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.LeavePeriod
import com.roni.gymcraft.engine.testing.Seasons
import com.roni.gymcraft.engine.waves.Campaign
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveCodecTest {
    private val c = DefaultContent
    private val start = Seasons.START

    /** A state with everything filled in: sessions, wounded, an occupied building, leave, and a few weeks of battles. */
    private fun richState() = run {
        var s = Seasons.newGame(seed = 5L)
        s = (Economy.session(s, start, start) as Change.Ok).state
        s = (Economy.train(s, c, Outpost.Rifleman, 2) as Change.Ok).state
        s = (Economy.homeCard(s, start, start) as Change.Ok).state
        s = Campaign.resolveDue(s, c, start.plusDays(13).atTime(21, 0)).state
        s.copy(leave = listOf(LeavePeriod(start.plusDays(14), start.plusDays(15)), LeavePeriod(start.plusDays(20))))
    }

    @Test
    fun roundTrip_keepsEverything() {
        val file = SaveFile(richState(), seenBattles = 3)
        assertTrue("the fixture should have battles, wounded and an occupied building",
            file.state.battles.size == 14 && file.state.buildings.values.any { it.occupied })
        val back = SaveCodec.decode(SaveCodec.encode(file))
        assertEquals(SaveCodec.Decoded.Ok(file), back)
    }

    @Test
    fun aNewerFile_isRefused_notGuessed() {
        val text = SaveCodec.encode(SaveFile(Seasons.newGame())).replaceFirst("\"version\":1", "\"version\":2")
        assertEquals(SaveCodec.Decoded.TooNew(2), SaveCodec.decode(text))
    }

    @Test
    fun garbage_isBroken_notACrash() {
        assertTrue(SaveCodec.decode("not json") is SaveCodec.Decoded.Broken)
        assertTrue(SaveCodec.decode("{\"version\":1}") is SaveCodec.Decoded.Broken)
        val cut = SaveCodec.encode(SaveFile(richState()))
        assertTrue(SaveCodec.decode(cut.substring(0, cut.length / 2)) is SaveCodec.Decoded.Broken)
    }

    @Test
    fun theFormat_isPlainAndStable() {
        val text = SaveCodec.encode(SaveFile(Seasons.newGame(seed = 1L)))
        assertTrue(text, text.startsWith("{\"version\":1,\"seenBattles\":0,\"state\":{\"race\":\"outpost\",\"enemy\":\"brood\",\"seasonStart\":\"2026-11-01\""))
        assertTrue(text, text.contains("\"resolvedThrough\":\"2026-11-01T00:00\""))
    }
}
