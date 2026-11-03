package com.roni.gymcraft.engine.model

import kotlinx.serialization.Serializable

/** Every game object is named by an id (architecture.md §4): races are data, never code. */
@Serializable @JvmInline value class RaceId(val value: String)
@Serializable @JvmInline value class BuildingId(val value: String)
@Serializable @JvmInline value class UnitId(val value: String)
@Serializable @JvmInline value class TraitId(val value: String)
@Serializable @JvmInline value class ArtId(val value: String)
