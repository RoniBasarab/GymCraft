package com.roni.gymcraft.feature.spritetest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class FpsMeterTest {

    @Test
    fun oneSecondOf60Frames() {
        val meter = FpsMeter()
        var result: FpsMeter.Frame? = null
        for (i in 0..60) {
            result = meter.frame(i * 16_670_000L) ?: result
        }

        assertNotNull(result)
        assertEquals(60, result?.fps)
        assertEquals(17, result?.worstMs)
    }
}
