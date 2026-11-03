package com.roni.gymcraft.core.game

import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class GameClockTest {
    private val real = LocalDateTime.of(2026, 11, 3, 9, 0)
    private val file = File(Files.createTempDirectory("clock").toFile(), "debug_clock.txt")

    @Test
    fun debugClock_goesForward_andRemembersAcrossARestart() {
        val clock = DebugClock(file) { real }
        clock.forward(90)
        clock.forward(-30)                       // never backwards
        assertEquals(real.plusMinutes(90), clock.now())
        assertEquals(real.plusMinutes(90), DebugClock(file) { real }.now())
        clock.reset()
        assertEquals(real, DebugClock(file) { real }.now())
    }

    @Test
    fun phoneClock_isTheRealTime_andIgnoresTravel() {
        val clock = PhoneClock { real }
        clock.forward(60)
        assertEquals(real, clock.now())
    }
}
