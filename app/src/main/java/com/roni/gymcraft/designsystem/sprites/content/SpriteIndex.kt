package com.roni.gymcraft.designsystem.sprites.content

import com.roni.gymcraft.designsystem.sprites.SpriteArt
import com.roni.gymcraft.engine.model.ArtId
import com.roni.gymcraft.engine.model.RaceId

/** Every sprite by its [ArtId] (races name their art by id; architecture.md §4). A missing id gives null: draw a placeholder. */
object SpriteIndex {
    private val byId: Map<ArtId, SpriteArt> by lazy { (OutpostArt.all + BroodArt.all).associateBy { it.art } }

    fun of(id: ArtId): SpriteArt? = byId[id]

    /** A race's drill sergeant: the art "<race>.sergeant". */
    fun portrait(race: RaceId): ArtId = ArtId("${race.value}.sergeant")
}
