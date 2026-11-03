package com.roni.gymcraft.core.game

import com.roni.gymcraft.core.save.SaveStore
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.DefaultMatchup
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.economy.Refusal
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRepositoryTest {
    private var now = LocalDateTime.of(2026, 11, 1, 9, 0)   // a Sunday morning
    private val dir: File = Files.createTempDirectory("game").toFile()
    private fun repo() = GameRepository(SaveStore(dir), { now }, DefaultContent, DefaultMatchup, Dispatchers.Unconfined, seed = { 7L })

    @Test
    fun firstOpen_startsAGame_andSavesIt() = runBlocking {
        val r = repo()
        assertEquals(Opened.Ok, r.open())
        val g = r.game.value ?: throw AssertionError("no game")
        assertEquals(100, g.state.supplies)
        assertEquals(now.toLocalDate(), g.state.seasonStart)
        assertTrue(File(dir, "save.json").exists())
        assertTrue(g.forecast != null)
    }

    @Test
    fun aSession_paysOnce_andSurvivesARestart() = runBlocking {
        val r = repo(); r.open()
        assertEquals(200, ((r.session() as Change.Ok).state.supplies))
        assertEquals(Refusal.ALREADY_TODAY, (r.session() as Change.Refused).why)
        assertEquals(50, (r.train(Outpost.Rifleman, 3) as Change.Ok).state.supplies)
        val again = repo(); again.open()
        assertEquals(mapOf(Outpost.Rifleman to 3), again.game.value?.state?.ready)
    }

    @Test
    fun timePassing_fightsTheBattles_andTheCardShowsThemUntilSeen() = runBlocking {
        val r = repo(); r.open()
        now = now.plusDays(6).withHour(21)   // Saturday 21:00: six raids and the boss are due
        r.refresh()
        val g = r.game.value ?: throw AssertionError()
        assertEquals(7, g.unseen.size)
        r.markSeen()
        assertTrue(r.game.value?.unseen?.isEmpty() == true)
        // A change after time passed fights first: a session the next week sees the week's battles already done.
        now = now.plusDays(2)
        r.session()
        assertEquals(9, r.game.value?.state?.battles?.size)
        assertEquals(2, r.game.value?.unseen?.size)
    }

    @Test
    fun aNewerSave_isNotLoaded_andNotOverwritten() = runBlocking {
        repo().open()
        val f = File(dir, "save.json")
        val text = f.readText().replaceFirst("\"version\":1", "\"version\":5")
        f.writeText(text)
        val r = repo()
        assertEquals(Opened.TooNew(5), r.open())
        assertNull(r.game.value)
        assertEquals(Change.Refused(Refusal.UNKNOWN), r.session())
        assertEquals(text, f.readText())
    }

    @Test
    fun anUnreadableSave_startsFresh_andKeepsTheOldFilesAside() = runBlocking {
        File(dir, "save.json").writeText("garbage")
        val opened = repo().open()
        assertTrue(opened is Opened.StartedFresh)
        assertTrue(dir.list()?.any { it.startsWith("save.json.unreadable-") } == true)
        assertTrue(File(dir, "save.json").exists())
    }
}
