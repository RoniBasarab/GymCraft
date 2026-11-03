package com.roni.gymcraft.designsystem.sprites

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.roni.gymcraft.engine.model.ArtId

/**
 * The game's sprites (game-design.md §10): each one is a few layers, the pieces code moves (cutout animation).
 * Every layer is an [ImageVector] the size of the whole sprite, so layers drawn at the same place line up.
 *
 * [SpriteLayer.part] says what moves it: "base" (still), "bob", "recoil", "flash", "spin", "turret", "stepA"/"stepB",
 * "blink", "wave", "swing", "glow", "smoke", "pulse", "scuttle", "legsA"/"legsB", "chomp"/"chompB", "spit", "breathe",
 * "bounce", "tread", "talk", "backblast". A team layer is drawn in [TEAM_FILL] and tinted to the player's color when drawn.
 * [SpriteLayer.bounds] = left, top, right, bottom of what the layer draws, in sprite units: pivots for rotations and
 * anchors (the flash's left edge, half way down, is the barrel tip: [SpriteArt.muzzle]).
 */
class SpriteLayer(val part: String, val team: Boolean, val bounds: FloatArray, val image: ImageVector) {
    val centerX: Float get() = (bounds[0] + bounds[2]) / 2f
    val centerY: Float get() = (bounds[1] + bounds[3]) / 2f
}

class SpriteArt(val art: ArtId, val width: Float, val height: Float, val layers: List<SpriteLayer>) {
    /** The barrel tip in sprite units (where a muzzle flash and a shot start), or null for a sprite without a gun. */
    val muzzle: Offset? = layers.firstOrNull { it.part == "flash" }?.let { Offset(it.bounds[0], (it.bounds[1] + it.bounds[3]) / 2f) }
}

/** The default team color (the Outpost's orange); team layers are drawn in it, then tinted. */
val TEAM_FILL: Brush = SolidColor(Color(0xFFE07B28))

class SpriteBuilder internal constructor(private val width: Float, private val height: Float) {
    internal val layers = ArrayList<SpriteLayer>()

    fun layer(part: String, team: Boolean, bounds: FloatArray, shapes: LayerBuilder.() -> Unit) {
        val builder = ImageVector.Builder(
            name = part,
            defaultWidth = width.dp,
            defaultHeight = height.dp,
            viewportWidth = width,
            viewportHeight = height,
        )
        LayerBuilder(builder).shapes()
        layers += SpriteLayer(part, team, bounds, builder.build())
    }
}

class LayerBuilder internal constructor(private val builder: ImageVector.Builder) {
    fun shape(fill: Brush?, stroke: Brush?, width: Float, cap: StrokeCap, join: StrokeJoin, alpha: Float, path: PathBuilder.() -> Unit) {
        builder.path(
            fill = fill,
            fillAlpha = alpha,
            stroke = stroke,
            strokeAlpha = alpha,
            strokeLineWidth = width,
            strokeLineCap = cap,
            strokeLineJoin = join,
            pathBuilder = path,
        )
    }
}

fun sprite(art: ArtId, width: Float, height: Float, layers: SpriteBuilder.() -> Unit): SpriteArt {
    val b = SpriteBuilder(width, height)
    b.layers()
    return SpriteArt(art, width, height, b.layers)
}
