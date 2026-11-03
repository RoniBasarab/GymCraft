package com.roni.gymcraft.engine.model

/**
 * What a race is made of (architecture.md §4). Plain data: the engine works only with these, never with a race by
 * name, so a new race is a new definition in content/ plus its art.
 */

/** Combat numbers of one unit (or the HQ). [range] and [speed] are in tiles and tiles per second. */
data class Stats(
    val hp: Double,
    val damage: Double,
    val cooldown: Double,
    val range: Double,
    val speed: Double,
)

data class UnitDef(
    val id: UnitId,
    val name: String,
    /** 1, 2 or 3: the level its building needs. */
    val tier: Int,
    /** Supplies to train one (for an enemy-only unit: its weight in a wave's budget). */
    val cost: Int,
    val stats: Stats,
    val trait: TraitId,
    /** Damage multipliers against targets with a trait: Rocketeer ×2 vs armored. Missing = ×1. */
    val bonus: Map<TraitId, Double> = emptyMap(),
    /** How many enemy units one shot can drop at most: 1 = no splash (a rocket can't kill four Biters at once). */
    val splash: Int = 1,
    /** Health restored per [Stats.cooldown] to an ally carrying [healsTag]; a healer deals no damage. */
    val heal: Double = 0.0,
    val healsTag: String? = null,
    /** Free labels other rules can refer to, e.g. "infantry" for the Medic. */
    val tags: Set<String> = emptySet(),
    val art: ArtId,
)

/** A production building: level n unlocks [units] up to index n-1. [upgradeCosts][i] buys level i+2. */
data class BuildingDef(
    val id: BuildingId,
    val name: String,
    val units: List<UnitId>,
    val upgradeCosts: List<Int>,
    val art: ArtId,
) {
    val maxLevel: Int get() = units.size
}

data class HqDef(val name: String, val stats: Stats, val art: ArtId)

data class RaceDef(
    val id: RaceId,
    val name: String,
    val hq: HqDef,
    val buildings: List<BuildingDef>,
    val units: List<UnitDef>,
) {
    fun unit(id: UnitId): UnitDef? = units.firstOrNull { it.id == id }
    fun building(id: BuildingId): BuildingDef? = buildings.firstOrNull { it.id == id }
    /** The building that trains [unit], or null. */
    fun buildingOf(unit: UnitId): BuildingDef? = buildings.firstOrNull { unit in it.units }
}

/** Every race the game knows. Passed in, never global, so tests can build their own. */
class GameContent(val races: List<RaceDef>) {
    fun race(id: RaceId): RaceDef? = races.firstOrNull { it.id == id }
    fun unit(id: UnitId): UnitDef? = races.firstNotNullOfOrNull { it.unit(id) }
}
