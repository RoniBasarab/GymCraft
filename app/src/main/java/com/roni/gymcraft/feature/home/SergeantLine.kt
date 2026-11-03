package com.roni.gymcraft.feature.home

import com.roni.gymcraft.engine.battle.SquadSpec
import com.roni.gymcraft.engine.economy.Change
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BattleOutcome
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.waves.Forecast
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

/**
 * What the drill sergeant says (game-design.md §9): tough, short, a bit funny, never cruel about missed weeks.
 * Pure: built from the engine's state and forecast, so every line is unit-tested. Names come from the race data.
 */
object SergeantLine {

    /** The line on Home, under the sergeant's portrait. */
    fun home(state: GameState, forecast: Forecast?, content: GameContent, now: LocalDateTime): String {
        val today = now.toLocalDate()
        if (state.onLeave(today)) return "On leave. Get well. The base will wait."
        if (today.isBefore(state.seasonStart)) {
            return "Season starts Sunday, recruit. Every session now is a head start."
        }
        if (forecast == null) return "Season's over, recruit. Stand easy."
        val wave = waveText(forecast.wave.squads, content)
        val day = if (forecast.battle.at.toLocalDate() == today) "tonight" else
            "by " + forecast.battle.at.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val counter = forecast.counter?.let { content.unit(it)?.name }
        val hint = if (counter != null) " ${forecast.wave.mainTrait.value.replaceFirstChar { it.uppercase() }} wave: ${plural(counter)} crack it." else ""
        return when (val k = forecast.sessionsNeeded) {
            0 -> "Coming ${day.removePrefix("by ")}: $wave. We're ready. Keep it that way."
            null -> "Coming ${day.removePrefix("by ")}: $wave. Too big for us yet. Every session still counts.$hint"
            1 -> "One more session $day, recruit, or that wall falls.$hint"
            else -> "$k more sessions $day, recruit, or that wall falls.$hint"
        }
    }

    /** What he says right after "I went" (and the hold-to-confirm finished). */
    fun session(change: Change.Ok): String =
        if (change.rally) "You're back. The troops noticed. Rally bonus: +${change.gained - 100}. +${change.gained} supplies."
        else "Good work, recruit. +${change.gained} supplies."

    /** One row of the "While you were away" card. */
    fun result(outcome: BattleOutcome, content: GameContent): String {
        val day = outcome.at.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        val kind = if (outcome.kind == BattleKind.BOSS) "Boss" else "Raid"
        val lost = outcome.fallen.values.sum()
        val losses = if (lost > 0) " $lost wounded." else ""
        return when {
            outcome.won -> "$kind $day: held.$losses"
            outcome.occupied != null -> {
                val building = content.race(outcome.input.defenderRace)?.building(outcome.occupied)?.name ?: "A building"
                "$kind $day: lost. The $building fell.$losses One gym session and we take it back."
            }
            else -> "$kind $day: lost.$losses"
        }
    }

    /** "30 Biters and 2 Brutes", biggest squads first, at most three kinds. */
    fun waveText(squads: List<SquadSpec>, content: GameContent): String {
        val parts = squads.sortedByDescending { it.count }.take(3).map { q ->
            val name = content.unit(q.unit)?.name ?: "enemies"
            "${q.count} ${if (q.count == 1) name else plural(name)}"
        }
        return when (parts.size) {
            0 -> "nothing"
            1 -> parts[0]
            else -> parts.dropLast(1).joinToString(", ") + " and " + parts.last()
        }
    }

    /** "Biters", "Riflemen", "Rocketeers"; a name ending in s stays. */
    fun plural(name: String): String = when {
        name.endsWith("man") -> name.dropLast(3) + "men"
        name.endsWith("s") -> name
        else -> name + "s"
    }

    /** Saturday is boss day; used by the UI for "by Saturday" headers. */
    val BOSS_DAY: DayOfWeek = DayOfWeek.SATURDAY
}
