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

/** The Outpost (game-design.md §7): first numbers, tuned by the balance test. */
object Outpost {
    val id = RaceId("outpost")
    val Barracks = BuildingId("outpost.barracks")
    val MotorPool = BuildingId("outpost.motor_pool")
    val Rifleman = UnitId("outpost.rifleman")
    val Medic = UnitId("outpost.medic")
    val Rocketeer = UnitId("outpost.rocketeer")
    val Buggy = UnitId("outpost.buggy")
    val Tank = UnitId("outpost.tank")
    val Walker = UnitId("outpost.walker")

    val race = RaceDef(
        id = id,
        name = "The Outpost",
        hq = HqDef("HQ", Stats(hp = 500.0, damage = 5.0, cooldown = 1.0, range = 4.0, speed = 0.0), ArtId("outpost.hq")),
        buildings = listOf(
            BuildingDef(Barracks, "Barracks", listOf(Rifleman, Medic, Rocketeer), listOf(150, 300), ArtId("outpost.barracks")),
            BuildingDef(MotorPool, "Motor Pool", listOf(Buggy, Tank, Walker), listOf(150, 300), ArtId("outpost.motor_pool")),
        ),
        units = listOf(
            UnitDef(Rifleman, "Rifleman", 1, 50, Stats(40.0, 6.0, 0.8, 3.0, 1.0), Traits.Light, tags = setOf("infantry"), art = ArtId("outpost.rifleman")),
            UnitDef(Medic, "Medic", 2, 100, Stats(45.0, 0.0, 1.0, 2.5, 1.0), Traits.Light, heal = 6.0, healsTag = "infantry", tags = setOf("infantry"), art = ArtId("outpost.medic")),
            UnitDef(Rocketeer, "Rocketeer", 3, 200, Stats(100.0, 72.0, 1.5, 3.5, 0.9), Traits.Light, bonus = mapOf(Traits.Armored to 2.0), tags = setOf("infantry"), art = ArtId("outpost.rocketeer")),
            UnitDef(Buggy, "Buggy", 1, 50, Stats(45.0, 4.0, 0.6, 2.0, 2.2), Traits.Light, art = ArtId("outpost.buggy")),
            UnitDef(Tank, "Tank", 2, 100, Stats(150.0, 18.0, 2.25, 4.5, 0.7), Traits.Armored, bonus = mapOf(Traits.Swarm to 2.0), splash = 4, art = ArtId("outpost.tank")),
            UnitDef(Walker, "Walker", 3, 200, Stats(400.0, 15.0, 1.25, 2.0, 0.5), Traits.Armored, art = ArtId("outpost.walker")),
        ),
    )
}
