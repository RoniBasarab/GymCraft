package com.roni.gymcraft.designsystem.sprites.content

import com.roni.gymcraft.designsystem.sprites.SpriteArt
import com.roni.gymcraft.engine.model.ArtId
import com.roni.gymcraft.engine.model.RaceId

/**
 * Every sprite by its [ArtId] (races name their art by id; architecture.md §4). A missing id gives null: draw a
 * placeholder. A race's extra art follows a naming convention, so no code outside content/ names a race:
 * "<race>.sergeant", "<race>.yard", "<race>.drill.<move>".
 */
object SpriteIndex {
    private val byId: Map<ArtId, SpriteArt> by lazy { (OutpostArt.all + BroodArt.all + UiArt.all).associateBy { it.art } }

    fun of(id: ArtId): SpriteArt? = byId[id]

    /** The currency's icon: a protein tub. */
    val PROTEIN = ArtId("ui.protein")

    /** A race's drill sergeant. */
    fun portrait(race: RaceId): ArtId = ArtId("${race.value}.sergeant")

    /** A race's training yard, drawn next to its HQ. */
    fun yard(race: RaceId): ArtId = ArtId("${race.value}.yard")

    /** A race's recruit doing a home-card move ("squat", "pushup", "situp", "jacks"). */
    fun drill(race: RaceId, move: String): ArtId = ArtId("${race.value}.drill.$move")
}
