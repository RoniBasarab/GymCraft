package com.roni.gymcraft.core.game

import java.io.File
import java.time.LocalDateTime

/** The time the game runs on (architecture.md §8). Every rule asks this, never `LocalDateTime.now()`. */
fun interface GameClock {
    fun now(): LocalDateTime
}

/** The game's clock, with debug time travel: moves it forward to test battles without waiting for Saturday. */
interface TimeTravel : GameClock {
    val enabled: Boolean
    val offsetMinutes: Long
    fun forward(minutes: Long)
    fun reset()
}

/** Release: the phone's own time, no travel. */
class PhoneClock(private val real: () -> LocalDateTime = LocalDateTime::now) : TimeTravel {
    override val enabled = false
    override val offsetMinutes = 0L
    override fun now(): LocalDateTime = real()
    override fun forward(minutes: Long) = Unit
    override fun reset() = Unit
}

/**
 * Debug: the phone's time plus an offset kept in [file] (it survives a restart, so the save never sits in the
 * clock's future). Only forward: going back would leave battles already fought "in the future". [reset] goes back
 * to real time anyway, for a fresh start (clear the debug app's data first).
 */
class DebugClock(private val file: File, private val real: () -> LocalDateTime = LocalDateTime::now) : TimeTravel {
    override val enabled = true

    @Volatile
    override var offsetMinutes: Long = file.takeIf { it.exists() }?.readText()?.trim()?.toLongOrNull() ?: 0L
        private set

    override fun now(): LocalDateTime = real().plusMinutes(offsetMinutes)

    override fun forward(minutes: Long) {
        if (minutes <= 0) return
        offsetMinutes += minutes
        file.writeText(offsetMinutes.toString())
    }

    override fun reset() {
        offsetMinutes = 0
        file.writeText("0")
    }
}
