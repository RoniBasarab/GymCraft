package com.roni.gymcraft.feature.home

import java.time.LocalDate

/**
 * One home exercise: what it says, the drill recruit's [move] ("squat", "pushup", "situp", "jacks": the art is
 * "<race>.drill.<move>"), and whether it's a [hold] (the recruit holds the move's second pose and shakes).
 */
data class HomeCard(val text: String, val move: String, val hold: Boolean = false)

/** The daily home card (game-design.md §3, §12): one small exercise a day, the same all day, +10 when done. */
object HomeCards {
    val all = listOf(
        HomeCard("15 squats", "squat"),
        HomeCard("10 push-ups", "pushup"),
        HomeCard("30-second plank", "pushup", hold = true),
        HomeCard("20 lunges", "squat"),
        HomeCard("15 sit-ups", "situp"),
        HomeCard("30 jumping jacks", "jacks"),
        HomeCard("10 burpees", "jacks"),
        HomeCard("20 glute bridges", "situp"),
        HomeCard("45-second wall sit", "squat", hold = true),
        HomeCard("12 chair dips", "pushup"),
    )

    /** The card for [day]: picked by the day and the season's seed, so it's fixed all day and differs by game. */
    fun forDay(day: LocalDate, seed: Long): HomeCard {
        val h = (day.toEpochDay() * 2_654_435_761L) xor seed
        return all[Math.floorMod(h xor (h ushr 17), all.size.toLong()).toInt()]
    }
}
