package com.roni.gymcraft.core.save

import com.roni.gymcraft.engine.content.Brood
import com.roni.gymcraft.engine.content.DefaultContent
import com.roni.gymcraft.engine.content.Outpost
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.save.SaveFile
import java.io.File
import java.nio.file.Files
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveStoreTest {
    private val start = LocalDate.of(2026, 11, 1)
    private fun game(supplies: Int) = SaveFile(Economy.newGame(DefaultContent, Outpost.id, Brood.id, start, start.atStartOfDay(), 1L).copy(supplies = supplies))
    private fun tempDir(): File = Files.createTempDirectory("save").toFile()

    @Test
    fun empty_thenSaved_thenLoaded() {
        val dir = tempDir(); val store = SaveStore(dir)
        assertEquals(SaveStore.Loaded.Empty, store.load())
        store.save(game(150))
        assertEquals(SaveStore.Loaded.Ok(game(150), fromBackup = false), store.load())
        assertTrue(!File(dir, "save.json.tmp").exists())
    }

    @Test
    fun theBackup_isThePreviousSave_andRescuesABrokenOne() {
        val dir = tempDir(); val store = SaveStore(dir)
        store.save(game(100)); store.save(game(200))
        File(dir, "save.json").writeText("{ half a fi")
        assertEquals(SaveStore.Loaded.Ok(game(100), fromBackup = true), store.load())
    }

    @Test
    fun bothBroken_areSetAside_neverDeleted() {
        val dir = tempDir(); val store = SaveStore(dir, now = { 42L })
        File(dir, "save.json").writeText("garbage")
        File(dir, "save.json.bak").writeText("more garbage")
        val loaded = store.load()
        assertTrue(loaded is SaveStore.Loaded.Unreadable)
        assertEquals(setOf("save.json.unreadable-42", "save.json.bak.unreadable-42"), dir.list()?.toSet())
        assertEquals("garbage", File(dir, "save.json.unreadable-42").readText())
        assertEquals(SaveStore.Loaded.Empty, store.load())
    }

    @Test
    fun aNewerSave_isLeftAlone() {
        val dir = tempDir(); val store = SaveStore(dir)
        store.save(game(100))
        val text = File(dir, "save.json").readText().replaceFirst("\"version\":1", "\"version\":9")
        File(dir, "save.json").writeText(text)
        assertEquals(SaveStore.Loaded.TooNew(9), store.load())
        assertEquals(text, File(dir, "save.json").readText())
    }
}
