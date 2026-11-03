package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.battle.BattleSim
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.model.UnitId
import java.time.LocalDateTime

/**
 * The forecast for the next boss (game-design.md §2, §4): what's coming, its main trait, the unit that counters it,
 * the chance to win as things stand, and how many more sessions would make it likely. "Likely" is a win in at least
 * [LIKELY] of [SAMPLES] simulated rolls; more sessions are spent by [Planner] the way a sensible player would.
 */
data class Forecast(
    val battle: ScheduledBattle,
    val wave: Wave,
    val counter: UnitId?,
    val winChance: Double,
    /** 0 = likely to win now; null = not even with [Forecaster.LOOKAHEAD] more sessions. */
    val sessionsNeeded: Int?,
)

object Forecaster {
    const val SAMPLES = 8
    const val LIKELY = 0.6
    const val LOOKAHEAD = 3

    fun nextBoss(state: GameState, content: GameContent, now: LocalDateTime): Forecast? {
        val battle = Schedule.nextBoss(state, now) ?: return null
        val wave = Waves.wave(state, content, battle)
        val counter = Planner.counter(state, content, wave.mainTrait, unlockedOnly = false)
        val chanceNow = winChance(state, content, battle)
        var needed: Int? = null
        var projected = state
        for (k in 0..LOOKAHEAD) {
            if (k > 0) {
                projected = projected.copy(
                    supplies = projected.supplies + Economy.SESSION,
                    ready = Economy.merge(projected.ready, projected.wounded),
                    wounded = emptyMap(),
                    buildings = projected.buildings.mapValues { (_, b) -> b.copy(occupied = false) },
                )
            }
            val spent = if (k == 0) projected else Planner.spend(projected, content, wave.mainTrait)
            val chance = if (k == 0) chanceNow else winChance(spent, content, battle)
            if (chance >= LIKELY) { needed = k; break }
        }
        return Forecast(battle, wave, counter, chanceNow, needed)
    }

    fun winChance(state: GameState, content: GameContent, battle: ScheduledBattle): Double {
        val base = Campaign.input(state, content, battle)
        val wins = (0 until SAMPLES).count { i -> BattleSim.run(base.copy(seed = base.seed + 7919L * (i + 1)), content).won }
        return wins.toDouble() / SAMPLES
    }
}
