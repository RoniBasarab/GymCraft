package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.testing.Seasons
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The design's promise (game-design.md §4), checked over whole seasons played by the reference player:
 * a steady 3 sessions a week wins the boss comfortably, 2 is about a coin flip, 1 loses, 0 never wins.
 * And the comeback promise: a missed week hurts, two hurt more, and going back recovers within two weeks.
 */
class BalanceTest {
    @Test
    fun steadyPaces_hitTheDesignOdds() {
        val p3 = Seasons.winRate(3); val p2 = Seasons.winRate(2); val p1 = Seasons.winRate(1); val p0 = Seasons.winRate(0)
        val line = "3/wk ${pct(p3)}  2/wk ${pct(p2)}  1/wk ${pct(p1)}  0/wk ${pct(p0)}"
        assertTrue(line, p3 >= 0.80)
        assertTrue(line, p2 in 0.40..0.70)
        assertTrue(line, p1 <= 0.15)
        assertTrue(line, p0 <= 0.05)
    }

    @Test
    fun missedWeeks_hurt_andComingBackRecovers() {
        val normal = Seasons.winRate(3, weeks = 7..7)
        val oneOff = Seasons.winRate(3, off = setOf(7), weeks = 7..7)
        val twoOff = Seasons.winRate(3, off = setOf(7, 8), weeks = 8..8)
        val back = Seasons.winRate(3, off = setOf(7, 8), weeks = 10..10)
        val line = "week 7 normal ${pct(normal)}, one week off ${pct(oneOff)}, second week off ${pct(twoOff)}, two weeks after coming back ${pct(back)}"
        assertTrue(line, oneOff < normal)
        assertTrue(line, twoOff < oneOff)
        assertTrue(line, back >= 0.55)
    }

    private fun pct(d: Double) = "${(d * 100).toInt()}%"
}
