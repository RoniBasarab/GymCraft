package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.model.TraitId
import com.roni.gymcraft.engine.model.UnitDef
import com.roni.gymcraft.engine.model.UnitId

/**
 * How a sensible player spends (the balance test's reference player, and the forecast's "what more sessions buy").
 * Upgrades a building when its next unit counters the coming trait, or when the army is already worth three times
 * the upgrade; otherwise buys the best unit for the coming trait; repeats until nothing is affordable.
 */
object Planner {
    fun spend(state: GameState, content: GameContent, focus: TraitId?): GameState {
        var s = state
        val race = content.race(s.race) ?: return s
        while (true) {
            val upgrade = race.buildings.firstOrNull { b ->
                val at = s.buildings[b.id] ?: return@firstOrNull false
                if (at.occupied || at.level >= b.maxLevel) return@firstOrNull false
                val cost = b.upgradeCosts.getOrNull(at.level - 1) ?: return@firstOrNull false
                if (s.supplies < cost) return@firstOrNull false
                val next = race.unit(b.units[at.level])
                val counters = focus != null && next != null && (next.bonus[focus] ?: 1.0) > 1.0
                counters || armyValue(s, content) >= 3 * cost
            }
            if (upgrade != null) {
                val change = Economy.upgrade(s, content, upgrade.id)
                if (change is com.roni.gymcraft.engine.economy.Change.Ok) { s = change.state; continue }
            }
            val unit = counter(s, content, focus, unlockedOnly = true, affordable = true) ?: break
            val change = Economy.train(s, content, unit)
            if (change is com.roni.gymcraft.engine.economy.Change.Ok) s = change.state else break
        }
        return s
    }

    /**
     * The unit with the best value against [focus]. Value is health × damage per second × the bonus, per supply squared
     * (in a squad fight, strength grows with the square of numbers, so cheap units count double). Units within 10% of the
     * best are equally good: the one the army has least of wins, so armies stay mixed.
     */
    fun counter(state: GameState, content: GameContent, focus: TraitId?, unlockedOnly: Boolean, affordable: Boolean = false): UnitId? {
        val race = content.race(state.race) ?: return null
        val candidates = race.units.filter { u ->
            if (u.heal > 0.0) return@filter false
            val b = race.buildingOf(u.id) ?: return@filter false
            val at = state.buildings[b.id]
            (!unlockedOnly || (at != null && !at.occupied && at.level >= u.tier)) && (!affordable || state.supplies >= u.cost)
        }
        val best = candidates.maxOfOrNull { score(it, focus) } ?: return null
        return candidates.filter { score(it, focus) >= best * 0.9 }
            .minByOrNull { ((state.ready[it.id] ?: 0) + (state.wounded[it.id] ?: 0)) * it.cost }?.id
    }

    fun score(u: UnitDef, focus: TraitId?): Double {
        val dps = if (u.stats.cooldown > 0) u.stats.damage / u.stats.cooldown else 0.0
        val bonus = focus?.let { u.bonus[it] } ?: 1.0
        return u.stats.hp * dps * bonus / (u.cost.toDouble() * u.cost)
    }

    fun armyValue(state: GameState, content: GameContent): Int =
        state.ready.entries.sumOf { (u, c) -> (content.unit(u)?.cost ?: 0) * c } +
            state.wounded.entries.sumOf { (u, c) -> (content.unit(u)?.cost ?: 0) * c }
}
