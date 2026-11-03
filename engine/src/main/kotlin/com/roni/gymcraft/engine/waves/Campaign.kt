package com.roni.gymcraft.engine.waves

import com.roni.gymcraft.engine.battle.BattleInput
import com.roni.gymcraft.engine.battle.BattleSim
import com.roni.gymcraft.engine.battle.Side
import com.roni.gymcraft.engine.battle.SquadSpec
import com.roni.gymcraft.engine.economy.Economy
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BattleOutcome
import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.GameState
import java.time.LocalDateTime

/**
 * Fights every battle that fell due (architecture.md §5): when the app opens or the notification job runs, whatever
 * came due since [GameState.resolvedThrough] is fought in order, each from the army as it stands after the one
 * before. Calling it twice with the same [now] changes nothing the second time.
 *
 * After a battle the defenders that fell are wounded (back after the next session). A lost boss occupies one
 * production building: the highest level one not occupied yet (game-design.md §5).
 */
object Campaign {
    data class Resolution(val state: GameState, val battles: List<BattleOutcome>)

    fun resolveDue(state: GameState, content: GameContent, now: LocalDateTime): Resolution {
        var s = state
        val fought = ArrayList<BattleOutcome>()
        for (battle in Schedule.due(state, now)) {
            val outcome = fight(s, content, battle)
            s = apply(s, outcome)
            fought += outcome
        }
        val through = if (now.isAfter(s.resolvedThrough)) now else s.resolvedThrough
        return Resolution(s.copy(resolvedThrough = through), fought)
    }

    /** The defenders as squads: every ready unit, at its building's level, in the race's own order. */
    fun army(state: GameState, content: GameContent): List<SquadSpec> {
        val race = content.race(state.race) ?: return emptyList()
        return race.units.mapNotNull { u ->
            val count = state.ready[u.id] ?: 0
            if (count <= 0) null
            else SquadSpec(u.id, count, race.buildingOf(u.id)?.let { state.level(it.id) } ?: 1)
        }
    }

    fun input(state: GameState, content: GameContent, battle: ScheduledBattle): BattleInput =
        BattleInput(state.race, army(state, content), state.enemy, Waves.wave(state, content, battle).squads, Waves.seed(state.seasonSeed, battle))

    private fun fight(state: GameState, content: GameContent, battle: ScheduledBattle): BattleOutcome {
        val input = input(state, content, battle)
        val result = BattleSim.run(input, content)
        val fallen = result.squads.filter { it.side == Side.DEFENDER && it.fallen > 0 }.associate { it.unit to it.fallen }
        val occupied = if (battle.kind == BattleKind.BOSS && !result.won) occupyTarget(state) else null
        return BattleOutcome(battle.at, battle.kind, battle.week, result.won, input, fallen, occupied)
    }

    private fun apply(state: GameState, outcome: BattleOutcome): GameState {
        val ready = state.ready.mapValues { (u, c) -> c - (outcome.fallen[u] ?: 0) }.filterValues { it > 0 }
        val buildings = outcome.occupied?.let { id -> state.buildings + (id to (state.buildings[id] ?: return@let state.buildings).copy(occupied = true)) }
            ?: state.buildings
        return state.copy(
            ready = ready,
            wounded = Economy.merge(state.wounded, outcome.fallen),
            buildings = buildings,
            resolvedThrough = outcome.at,
            battles = state.battles + outcome,
        )
    }

    private fun occupyTarget(state: GameState): BuildingId? =
        state.buildings.entries.filter { !it.value.occupied }.maxByOrNull { it.value.level }?.key
}
