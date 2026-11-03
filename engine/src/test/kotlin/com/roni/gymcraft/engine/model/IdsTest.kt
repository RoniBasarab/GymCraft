package com.roni.gymcraft.engine.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class IdsTest {

    @Test
    fun idsCompareByValue() {
        assertEquals(UnitId("a"), UnitId("a"))
        assertNotEquals(UnitId("a"), UnitId("b"))
    }
}
