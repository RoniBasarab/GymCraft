package com.roni.gymcraft.core.logging

import java.io.File

/**
 * The app's log (from Blocks 3.5.0): one file, `files/logs/gymcraft.log`, trimmed to its last 400 lines past 1 MB.
 * [echo] also sends each line to Logcat (wired in `AppModule`). Plain JVM, so tests use a temp file.
 */
class AppLogger(private val file: File, private val echo: (String) -> Unit = {}) {

    fun info(message: String) = write("I", message, null)

    fun warn(message: String) = write("W", message, null)

    fun error(message: String, error: Throwable? = null) = write("E", message, error)

    /** The log file, trimmed, for sharing. */
    fun shareFile(): File {
        runCatching { trim() }
        return file
    }

    private fun write(level: String, message: String, error: Throwable?) {
        val line = "$level ${System.currentTimeMillis()} $message ${error?.stackTraceToString().orEmpty()}"
        echo(line)
        runCatching {
            file.parentFile?.mkdirs()
            file.appendText(line.take(2000) + "\n")
            trim()
        }
    }

    private fun trim() {
        if (file.length() <= 1_000_000) return
        val tail = file.readLines().takeLast(400)
        file.writeText(tail.joinToString("\n"))
    }
}
