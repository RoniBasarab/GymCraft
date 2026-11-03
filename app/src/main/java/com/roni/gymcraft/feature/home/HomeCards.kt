package com.roni.gymcraft.feature.home

import java.time.LocalDate

/** The daily home card (game-design.md §3): one small exercise a day, the same all day, +10 when done. */
object HomeCards {
    val all = listOf(
        "15 squats",
        "10 push-ups",
        "30-second plank",
        "20 lunges",
        "15 sit-ups",
        "30 jumping jacks",
        "10 burpees",
        "20 glute bridges",
        "45-second wall sit",
        "12 chair dips",
    )

    /** The card for [day]: picked by the day and the season's seed, so it's fixed all day and differs by game. */
    fun forDay(day: LocalDate, seed: Long): String {
        val h = (day.toEpochDay() * 2_654_435_761L) xor seed
        return all[Math.floorMod(h xor (h ushr 17), all.size.toLong()).toInt()]
    }
}
