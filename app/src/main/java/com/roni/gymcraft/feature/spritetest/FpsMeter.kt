package com.roni.gymcraft.feature.spritetest

/** Counts frames and the worst frame gap over each second; returns a result once a second, else null. */
class FpsMeter {

    data class Frame(val fps: Int, val worstMs: Int)

    private var started = false
    private var startNanos = 0L
    private var lastNanos = 0L
    private var frames = 0
    private var worstNanos = 0L

    fun frame(now: Long): Frame? {
        if (!started) {
            started = true
            startNanos = now
            lastNanos = now
            return null
        }

        val gap = now - lastNanos
        lastNanos = now
        frames++
        if (gap > worstNanos) worstNanos = gap

        val elapsed = now - startNanos
        if (elapsed < 1_000_000_000L) return null

        val fps = ((frames * 1_000_000_000L + elapsed / 2) / elapsed).toInt()
        val worstMs = ((worstNanos + 500_000L) / 1_000_000L).toInt()
        startNanos = now
        frames = 0
        worstNanos = 0
        return Frame(fps, worstMs)
    }
}
