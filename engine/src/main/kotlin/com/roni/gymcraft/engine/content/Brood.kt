package com.roni.gymcraft.engine.content

import com.roni.gymcraft.engine.model.ArtId
import com.roni.gymcraft.engine.model.BuildingDef
import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.HqDef
import com.roni.gymcraft.engine.model.RaceDef
import com.roni.gymcraft.engine.model.RaceId
import com.roni.gymcraft.engine.model.Stats
import com.roni.gymcraft.engine.model.UnitDef
import com.roni.gymcraft.engine.model.UnitId

/** The Brood: season 1's attackers (game-design.md §7). Cost = weight in a wave's budget. */
object Brood {
    val id = RaceId("brood")
    val Nest = BuildingId("brood.nest")
    val Biter = UnitId("brood.biter")
    val Spitter = UnitId("brood.spitter")
    val Brute = UnitId("brood.brute")

    val race = RaceDef(
        id = id,
        name = "The Brood",
        hq = HqDef("Hive", Stats(hp = 1000.0, damage = 8.0, cooldown = 1.0, range = 4.0, speed = 0.0), ArtId("brood.hive")),
        buildings = listOf(
            BuildingDef(Nest, "Nest", listOf(Biter, Spitter, Brute), listOf(150, 300), ArtId("brood.nest")),
        ),
        units = listOf(
            UnitDef(Biter, "Biter", 1, 15, Stats(18.0, 1.2, 0.8, 0.6, 1.8), Traits.Swarm, art = ArtId("brood.biter")),
            UnitDef(Spitter, "Spitter", 2, 30, Stats(24.0, 6.3, 1.4, 3.0, 1.1), Traits.Light, art = ArtId("brood.spitter")),
            UnitDef(Brute, "Brute", 3, 120, Stats(240.0, 11.5, 1.6, 0.6, 0.7), Traits.Armored, art = ArtId("brood.brute")),
        ),
    )
}
