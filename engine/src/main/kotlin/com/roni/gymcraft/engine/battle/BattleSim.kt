package com.roni.gymcraft.engine.battle

import com.roni.gymcraft.engine.model.GameContent
import com.roni.gymcraft.engine.model.RaceId
import com.roni.gymcraft.engine.model.UnitDef
import com.roni.gymcraft.engine.model.UnitId
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.random.Random

/** One squad going into a battle: [count] units of [unit], trained by a building at [level] (+10% per level above 1). */
data class SquadSpec(val unit: UnitId, val count: Int, val level: Int = 1)

/**
 * Everything a battle needs. The same input always gives the same battle, so the save keeps inputs and
 * Rewatch simply runs them again (architecture.md §5).
 */
data class BattleInput(
    val defenderRace: RaceId,
    val defenders: List<SquadSpec>,
    val attackerRace: RaceId,
    val attackers: List<SquadSpec>,
    val seed: Long,
)

enum class Side { DEFENDER, ATTACKER }

data class SquadOutcome(val unit: UnitId, val side: Side, val start: Int, val left: Int) {
    val fallen: Int get() = start - left
}

data class BattleResult(
    val won: Boolean,
    val ticks: Int,
    val squads: List<SquadOutcome>,
    val hqHpLeft: Double,
    /** Frame by frame, only when asked for (playback). */
    val record: BattleRecord?,
)

/** What one squad did in a tick, for drawing shots and heals. [to] = -1 means the HQ. */
data class BattleEvent(val kind: Kind, val from: Int, val to: Int) {
    enum class Kind { SHOT, HEAL }
}

/**
 * Per tick: each squad's position (tiles from the HQ), its count, the HQ's health and that tick's events.
 * Squad index i matches [BattleRecord.units][i] and [BattleRecord.sides][i]; [hqX] is 0.
 */
class BattleRecord(
    val tickSeconds: Double,
    val units: List<UnitId>,
    val sides: List<Side>,
    val hqMaxHp: Double,
    val frames: List<Frame>,
) {
    class Frame(val x: FloatArray, val count: IntArray, val hqHp: Float, val events: List<BattleEvent>)
}

/**
 * The battle (game-design.md §6), pure and deterministic. A lane from the HQ (x = 0) out to the right; the attackers
 * walk in from [ATTACKER_START], the defenders advance at most to [DEFENDER_HOLD]. Squads, not single units: a squad's
 * health is its count × the unit's health and its count drops as health falls, so its damage falls with it.
 *
 * Each tick, every squad (and the HQ) picks the nearest enemy, closes in until it is in range, and fires when its
 * cooldown is up: count × damage × the trait bonus × a ±15% roll, where one shot can drop at most its splash count of
 * units (no overkill: a rocket kills one Biter, not four). All of a tick's damage lands at once, so no squad
 * gains from being first in the list. A healer heals the most hurt ally carrying its tag in range instead of firing.
 *
 * It ends when no attacker is left (won), when the HQ falls (lost), or after [MAX_TICKS] (the HQ held: won).
 * Talks to: nothing. Content comes in as [GameContent].
 */
object BattleSim {
    const val TICK = 0.1
    const val MAX_TICKS = 1200
    const val ATTACKER_START = 24.0
    const val DEFENDER_START = 2.0
    const val DEFENDER_HOLD = 6.0
    const val LEVEL_BOOST = 0.10
    private const val HQ = -1

    fun run(input: BattleInput, content: GameContent, record: Boolean = false): BattleResult {
        val defenderRace = content.race(input.defenderRace)
        val attackerRace = content.race(input.attackerRace)
        val hqStats = defenderRace?.hq?.stats

        val specs = input.defenders.filter { it.count > 0 }.map { it to Side.DEFENDER } +
            input.attackers.filter { it.count > 0 }.map { it to Side.ATTACKER }
        val resolved = specs.mapNotNull { (spec, side) ->
            val def = (if (side == Side.DEFENDER) defenderRace else attackerRace)?.unit(spec.unit) ?: content.unit(spec.unit)
            def?.let { Triple(spec, side, it) }
        }
        val n = resolved.size
        val defs: List<UnitDef> = resolved.map { it.third }
        val side = Array(n) { resolved[it].second }
        val start = IntArray(n) { resolved[it].first.count }
        val boost = DoubleArray(n) { 1.0 + LEVEL_BOOST * (resolved[it].first.level - 1).coerceAtLeast(0) }
        val unitHp = DoubleArray(n) { defs[it].stats.hp * boost[it] }
        val hp = DoubleArray(n) { unitHp[it] * start[it] }
        val maxHp = DoubleArray(n) { hp[it] }
        val x = DoubleArray(n) { i ->
            if (side[i] == Side.DEFENDER) DEFENDER_START else ATTACKER_START + 0.5 * attackerOrdinal(side, i)
        }
        val cooldown = DoubleArray(n)
        val pending = DoubleArray(n)
        var hqHp = hqStats?.hp ?: 0.0
        val hqMax = hqHp
        var hqCooldown = 0.0
        var hqPending = 0.0
        val rng = Random(input.seed)
        val frames = if (record) ArrayList<BattleRecord.Frame>() else null

        fun count(i: Int): Int = if (hp[i] <= 1e-9) 0 else ceil(hp[i] / unitHp[i] - 1e-9).toInt()
        fun alive(i: Int) = hp[i] > 1e-9
        fun anyAlive(s: Side) = (0 until n).any { side[it] == s && alive(it) }

        var ticks = 0
        var won: Boolean? = if (!anyAlive(Side.ATTACKER)) true else null
        while (won == null && ticks < MAX_TICKS) {
            ticks++
            val events = if (record) ArrayList<BattleEvent>() else null

            // 1. Move: close in on the nearest enemy until in range.
            for (i in 0 until n) {
                if (!alive(i)) continue
                val tx: Double
                val reach: Double
                if (defs[i].heal > 0.0) {
                    // A healer stays one tile behind its side's front squad.
                    val front = frontAlly(i, side, x, ::alive, n) ?: continue
                    tx = if (side[i] == Side.DEFENDER) x[front] - 1.0 else x[front] + 1.0
                    reach = 0.0
                } else {
                    val target = nearestEnemy(i, side, x, ::alive, n) ?: continue
                    tx = if (target == HQ) 0.0 else x[target]
                    reach = defs[i].stats.range
                }
                val gap = abs(x[i] - tx)
                if (gap > reach) {
                    val step = (defs[i].stats.speed * TICK).coerceAtMost(gap - reach)
                    x[i] += if (tx < x[i]) -step else step
                    if (side[i] == Side.DEFENDER) x[i] = x[i].coerceAtMost(DEFENDER_HOLD)
                    else x[i] = x[i].coerceAtLeast(0.4)
                }
            }

            // 2. Act: every squad and the HQ fire (or heal) from the positions and counts at the start of the tick.
            for (i in 0 until n) {
                if (!alive(i)) continue
                cooldown[i] -= TICK
                if (cooldown[i] > 1e-9) continue
                val def = defs[i]
                if (def.heal > 0.0) {
                    val ally = mostHurtAlly(i, side, x, hp, maxHp, defs, def, n) ?: continue
                    hp[ally] = (hp[ally] + count(i) * def.heal * boost[i]).coerceAtMost(maxHp[ally])
                    cooldown[i] = def.stats.cooldown
                    events?.add(BattleEvent(BattleEvent.Kind.HEAL, i, ally))
                    continue
                }
                val target = nearestEnemy(i, side, x, ::alive, n) ?: continue
                val tx = if (target == HQ) 0.0 else x[target]
                if (abs(x[i] - tx) > def.stats.range + 1e-9) continue
                val targetTrait = if (target == HQ) null else defs[target].trait
                val bonus = targetTrait?.let { def.bonus[it] } ?: 1.0
                // One shot drops at most [UnitDef.splash] units: damage beyond that is overkill and lost.
                val perShot = def.stats.damage * boost[i] * bonus * (0.85 + 0.3 * rng.nextDouble())
                val cap = if (target == HQ) perShot else unitHp[target] * def.splash
                val damage = count(i) * minOf(perShot, cap)
                if (target == HQ) hqPending += damage else pending[target] += damage
                cooldown[i] = def.stats.cooldown
                events?.add(BattleEvent(BattleEvent.Kind.SHOT, i, target))
            }
            if (hqStats != null && hqHp > 0.0) {
                hqCooldown -= TICK
                if (hqCooldown <= 1e-9) {
                    val target = (0 until n).filter { side[it] == Side.ATTACKER && alive(it) && x[it] <= hqStats.range + 1e-9 }
                        .minByOrNull { x[it] }
                    if (target != null) {
                        pending[target] += hqStats.damage * (0.85 + 0.3 * rng.nextDouble())
                        hqCooldown = hqStats.cooldown
                        events?.add(BattleEvent(BattleEvent.Kind.SHOT, HQ, target))
                    }
                }
            }

            // 3. Land it all at once.
            for (i in 0 until n) {
                if (pending[i] > 0.0) { hp[i] = (hp[i] - pending[i]).coerceAtLeast(0.0); pending[i] = 0.0 }
            }
            hqHp = (hqHp - hqPending).coerceAtLeast(0.0)
            hqPending = 0.0

            frames?.add(BattleRecord.Frame(FloatArray(n) { x[it].toFloat() }, IntArray(n) { count(it) }, hqHp.toFloat(), events ?: emptyList()))

            won = when {
                !anyAlive(Side.ATTACKER) -> true
                hqStats == null && !anyAlive(Side.DEFENDER) -> false
                hqStats != null && hqHp <= 0.0 -> false
                else -> null
            }
        }
        val outcome = won ?: true
        val squads = (0 until n).map { SquadOutcome(defs[it].id, side[it], start[it], count(it)) }
        val battleRecord = frames?.let { BattleRecord(TICK, defs.map { it.id }, side.toList(), hqMax, it) }
        return BattleResult(outcome, ticks, squads, hqHp, battleRecord)
    }

    private fun attackerOrdinal(side: Array<Side>, i: Int): Int = (0 until i).count { side[it] == Side.ATTACKER }

    /** The nearest living enemy squad; for an attacker with no defender squad left, the HQ. Null when none. */
    private fun nearestEnemy(i: Int, side: Array<Side>, x: DoubleArray, alive: (Int) -> Boolean, n: Int): Int? {
        var best: Int? = null
        var bestGap = Double.MAX_VALUE
        for (j in 0 until n) {
            if (side[j] == side[i] || !alive(j)) continue
            val gap = abs(x[i] - x[j])
            if (gap < bestGap) { bestGap = gap; best = j }
        }
        if (best == null && side[i] == Side.ATTACKER) return HQ
        return best
    }

    /** The ally furthest toward the enemy (other than [i]), or null. */
    private fun frontAlly(i: Int, side: Array<Side>, x: DoubleArray, alive: (Int) -> Boolean, n: Int): Int? {
        var best: Int? = null
        for (j in 0 until n) {
            if (j == i || side[j] != side[i] || !alive(j)) continue
            val b = best
            if (b == null || (side[i] == Side.DEFENDER && x[j] > x[b]) || (side[i] == Side.ATTACKER && x[j] < x[b])) best = j
        }
        return best
    }

    private fun mostHurtAlly(
        i: Int, side: Array<Side>, x: DoubleArray, hp: DoubleArray, maxHp: DoubleArray,
        defs: List<UnitDef>, healer: UnitDef, n: Int,
    ): Int? {
        val tag = healer.healsTag ?: return null
        var best: Int? = null
        var bestMissing = 0.0
        for (j in 0 until n) {
            if (j == i || side[j] != side[i] || hp[j] <= 1e-9 || tag !in defs[j].tags) continue
            if (abs(x[j] - x[i]) > healer.stats.range + 1e-9) continue
            val missing = maxHp[j] - hp[j]
            if (missing > bestMissing + 1e-9) { bestMissing = missing; best = j }
        }
        return best
    }
}
