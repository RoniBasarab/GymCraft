package com.roni.gymcraft.core.save

import com.roni.gymcraft.engine.save.SaveCodec
import com.roni.gymcraft.engine.save.SaveFile
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * The save file on disk (architecture.md §7): `save.json`, its backup `save.json.bak`, written through `save.json.tmp`.
 *
 * Writing: the new text goes to the temp file, the current save is copied to the backup, then the temp file is moved
 * over the save in one atomic step. A crash at any point leaves a whole save or a whole backup, never half a file.
 *
 * Reading: the save, else the backup. A file newer than this app is left alone ([Loaded.TooNew]: never overwritten).
 * When neither can be read, both are set aside under new names (never deleted) and the game starts fresh.
 * Plain java.io: no Android, so it's tested on the JVM with a temp folder. Talks to: [SaveCodec].
 */
class SaveStore(private val dir: File, private val now: () -> Long = System::currentTimeMillis) {
    sealed interface Loaded {
        data object Empty : Loaded
        data class Ok(val file: SaveFile, val fromBackup: Boolean) : Loaded
        data class TooNew(val version: Int) : Loaded
        data class Unreadable(val setAside: List<File>) : Loaded
    }

    private val main get() = File(dir, "save.json")
    private val backup get() = File(dir, "save.json.bak")
    private val temp get() = File(dir, "save.json.tmp")

    fun load(): Loaded {
        if (!main.exists() && !backup.exists()) return Loaded.Empty
        val fromMain = read(main)
        if (fromMain is SaveCodec.Decoded.Ok) return Loaded.Ok(fromMain.file, fromBackup = false)
        if (fromMain is SaveCodec.Decoded.TooNew) return Loaded.TooNew(fromMain.version)
        val fromBackup = read(backup)
        if (fromBackup is SaveCodec.Decoded.Ok) return Loaded.Ok(fromBackup.file, fromBackup = true)
        if (fromBackup is SaveCodec.Decoded.TooNew) return Loaded.TooNew(fromBackup.version)
        val stamp = now()
        val aside = listOf(main, backup).filter { it.exists() }.map { f ->
            val target = File(dir, "${f.name}.unreadable-$stamp")
            Files.move(f.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
            target
        }
        return Loaded.Unreadable(aside)
    }

    /** Writes [file]; throws when the disk refuses (the caller reports it). */
    fun save(file: SaveFile) {
        dir.mkdirs()
        temp.writeText(SaveCodec.encode(file))
        if (main.exists()) Files.copy(main.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.move(temp.toPath(), main.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
    }

    private fun read(f: File): SaveCodec.Decoded? =
        if (!f.exists()) null else try { SaveCodec.decode(f.readText()) } catch (e: Exception) { SaveCodec.Decoded.Broken(e.message.orEmpty()) }
}
