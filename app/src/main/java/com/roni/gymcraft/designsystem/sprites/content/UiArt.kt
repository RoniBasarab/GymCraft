package com.roni.gymcraft.designsystem.sprites.content

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.roni.gymcraft.designsystem.sprites.SpriteArt
import com.roni.gymcraft.designsystem.sprites.TEAM_FILL
import com.roni.gymcraft.designsystem.sprites.sprite
import com.roni.gymcraft.engine.model.ArtId

/** Icons that belong to no race: the protein tub (the currency). Generated from the "GymCraft Sprites" canvas by the planning agent; do not hand-edit. */
object UiArt {

    val Protein: SpriteArt by lazy {
        sprite(ArtId("ui.protein"), 64f, 64f) {
            layer("base", team = false, bounds = floatArrayOf(9.5f, 10.5f, 54.5f, 61f)) {
                shape(fill = SolidColor(Color(0xFFF1E9DA)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(14f, 22f)
                    lineTo(50f, 22f)
                    lineTo(47f, 58f)
                    quadTo(32f, 61f, 17f, 58f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFE07B28)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(16f, 31f)
                    lineTo(48f, 31f)
                    lineTo(46.6f, 48f)
                    lineTo(17.4f, 48f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(15f, 12f)
                    horizontalLineTo(49f)
                    arcTo(4f, 4f, 0f, false, true, 53f, 16f)
                    verticalLineTo(19f)
                    arcTo(4f, 4f, 0f, false, true, 49f, 23f)
                    horizontalLineTo(15f)
                    arcTo(4f, 4f, 0f, false, true, 11f, 19f)
                    verticalLineTo(16f)
                    arcTo(4f, 4f, 0f, false, true, 15f, 12f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF1C1915)), stroke = null, width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(25.2f, 38f)
                    horizontalLineTo(38.8f)
                    arcTo(1.2f, 1.2f, 0f, false, true, 40f, 39.2f)
                    verticalLineTo(39.8f)
                    arcTo(1.2f, 1.2f, 0f, false, true, 38.8f, 41f)
                    horizontalLineTo(25.2f)
                    arcTo(1.2f, 1.2f, 0f, false, true, 24f, 39.8f)
                    verticalLineTo(39.2f)
                    arcTo(1.2f, 1.2f, 0f, false, true, 25.2f, 38f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF1C1915)), stroke = null, width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22.5f, 34.5f)
                    horizontalLineTo(24f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 25.5f, 36f)
                    verticalLineTo(43f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 24f, 44.5f)
                    horizontalLineTo(22.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 21f, 43f)
                    verticalLineTo(36f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 22.5f, 34.5f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF1C1915)), stroke = null, width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(40f, 34.5f)
                    horizontalLineTo(41.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 43f, 36f)
                    verticalLineTo(43f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 41.5f, 44.5f)
                    horizontalLineTo(40f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 38.5f, 43f)
                    verticalLineTo(36f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 40f, 34.5f)
                    close()
                }
            }
        }
    }

    val all: List<SpriteArt> by lazy { listOf(Protein) }
}
