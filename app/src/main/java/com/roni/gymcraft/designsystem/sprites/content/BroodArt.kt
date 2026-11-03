package com.roni.gymcraft.designsystem.sprites.content

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.roni.gymcraft.designsystem.sprites.SpriteArt
import com.roni.gymcraft.designsystem.sprites.TEAM_FILL
import com.roni.gymcraft.designsystem.sprites.sprite
import com.roni.gymcraft.engine.model.ArtId

/** The Brood's sprites (season 1's attackers). Generated from the "GymCraft Sprites" canvas by the planning agent; do not hand-edit. */
object BroodArt {

    val Biter: SpriteArt by lazy {
        sprite(ArtId("brood.biter"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(28f, 91f, 96f, 105f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(28f, 98f)
                    arcTo(34f, 7f, 0f, true, false, 96f, 98f)
                    arcTo(34f, 7f, 0f, true, false, 28f, 98f)
                    close()
                }
            }
            layer("legsA", team = false, bounds = floatArrayOf(38f, 78f, 70f, 98f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(48f, 80f)
                    lineTo(40f, 94f)
                    moveTo(68f, 82f)
                    lineTo(64f, 96f)
                }
            }
            layer("legsB", team = false, bounds = floatArrayOf(50f, 78f, 82f, 98f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(58f, 82f)
                    lineTo(52f, 96f)
                    moveTo(78f, 80f)
                    lineTo(80f, 94f)
                }
            }
            layer("scuttle", team = false, bounds = floatArrayOf(18.5f, 48.5f, 95.5f, 91.5f)) {
                shape(fill = SolidColor(Color(0xFF7B3F6B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(34f, 70f)
                    arcTo(30f, 20f, 0f, true, false, 94f, 70f)
                    arcTo(30f, 20f, 0f, true, false, 34f, 70f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF4A2341)), width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(50f, 54f)
                    quadTo(56f, 70f, 50f, 88f)
                    moveTo(66f, 50f)
                    quadTo(72f, 70f, 66f, 90f)
                    moveTo(80f, 54f)
                    quadTo(86f, 70f, 82f, 86f)
                }
                shape(fill = null, stroke = SolidColor(Color(0xFFA85A8F)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(44f, 58f)
                    quadTo(60f, 46f, 82f, 52f)
                }
                shape(fill = SolidColor(Color(0xFFD77A9B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(20f, 74f)
                    arcTo(14f, 12f, 0f, true, false, 48f, 74f)
                    arcTo(14f, 12f, 0f, true, false, 20f, 74f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFE066)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(27f, 69f)
                    arcTo(3f, 3f, 0f, true, false, 33f, 69f)
                    arcTo(3f, 3f, 0f, true, false, 27f, 69f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFE066)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(35f, 67f)
                    arcTo(3f, 3f, 0f, true, false, 41f, 67f)
                    arcTo(3f, 3f, 0f, true, false, 35f, 67f)
                    close()
                }
            }
            layer("chomp", team = false, bounds = floatArrayOf(10.25f, 60.25f, 25.75f, 73.75f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(24f, 72f)
                    quadTo(14f, 70f, 12f, 62f)
                }
            }
            layer("chompB", team = false, bounds = floatArrayOf(10.25f, 78.25f, 25.75f, 91.75f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(24f, 80f)
                    quadTo(14f, 82f, 12f, 90f)
                }
            }
        }
    }

    val Spitter: SpriteArt by lazy {
        sprite(ArtId("brood.spitter"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(32f, 44.5f, 92f, 109f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(32f, 102f)
                    arcTo(30f, 7f, 0f, true, false, 92f, 102f)
                    arcTo(30f, 7f, 0f, true, false, 32f, 102f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(52f, 90f)
                    lineTo(46f, 102f)
                    moveTo(70f, 90f)
                    lineTo(74f, 102f)
                }
                shape(fill = SolidColor(Color(0xFF7B3F6B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(40f, 70f)
                    arcTo(22f, 24f, 0f, true, false, 84f, 70f)
                    arcTo(22f, 24f, 0f, true, false, 40f, 70f)
                    close()
                }
            }
            layer("pulse", team = false, bounds = floatArrayOf(56.5f, 32.5f, 91.5f, 67.5f)) {
                shape(fill = SolidColor(Color(0xFF9BE564)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(58f, 50f)
                    arcTo(16f, 16f, 0f, true, false, 90f, 50f)
                    arcTo(16f, 16f, 0f, true, false, 58f, 50f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFD9F7B8)), stroke = null, width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 45f)
                    arcTo(4f, 4f, 0f, true, false, 74f, 45f)
                    arcTo(4f, 4f, 0f, true, false, 66f, 45f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(28f, 50.75f, 51.5f, 72f)) {
                shape(fill = SolidColor(Color(0xFFD77A9B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(50f, 56f)
                    quadTo(40f, 50f, 34f, 54f)
                    quadTo(28f, 58f, 30f, 66f)
                    quadTo(34f, 72f, 46f, 70f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFE066)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(35.2f, 58f)
                    arcTo(2.8f, 2.8f, 0f, true, false, 40.8f, 58f)
                    arcTo(2.8f, 2.8f, 0f, true, false, 35.2f, 58f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFE066)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(41.2f, 56f)
                    arcTo(2.8f, 2.8f, 0f, true, false, 46.8f, 56f)
                    arcTo(2.8f, 2.8f, 0f, true, false, 41.2f, 56f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(30f, 64f)
                    quadTo(34f, 66f, 38f, 64f)
                }
            }
            layer("spit", team = false, bounds = floatArrayOf(20f, 58f, 32f, 70f)) {
                shape(fill = SolidColor(Color(0xFF9BE564)), stroke = SolidColor(Color(0xFF15110D)), width = 2f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 1f) {
                    moveTo(21f, 64f)
                    arcTo(5f, 5f, 0f, true, false, 31f, 64f)
                    arcTo(5f, 5f, 0f, true, false, 21f, 64f)
                    close()
                }
            }
        }
    }

    val Brute: SpriteArt by lazy {
        sprite(ArtId("brood.brute"), 130f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(20f, 78.5f, 112f, 112f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(20f, 104f)
                    arcTo(46f, 8f, 0f, true, false, 112f, 104f)
                    arcTo(46f, 8f, 0f, true, false, 20f, 104f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4A2341)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(63f, 80f)
                    horizontalLineTo(67f)
                    arcTo(5f, 5f, 0f, false, true, 72f, 85f)
                    verticalLineTo(97f)
                    arcTo(5f, 5f, 0f, false, true, 67f, 102f)
                    horizontalLineTo(63f)
                    arcTo(5f, 5f, 0f, false, true, 58f, 97f)
                    verticalLineTo(85f)
                    arcTo(5f, 5f, 0f, false, true, 63f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4A2341)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(89f, 80f)
                    horizontalLineTo(93f)
                    arcTo(5f, 5f, 0f, false, true, 98f, 85f)
                    verticalLineTo(97f)
                    arcTo(5f, 5f, 0f, false, true, 93f, 102f)
                    horizontalLineTo(89f)
                    arcTo(5f, 5f, 0f, false, true, 84f, 97f)
                    verticalLineTo(85f)
                    arcTo(5f, 5f, 0f, false, true, 89f, 80f)
                    close()
                }
            }
            layer("breathe", team = false, bounds = floatArrayOf(8f, 28.25f, 113.5f, 93.5f)) {
                shape(fill = SolidColor(Color(0xFF7B3F6B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(34f, 86f)
                    quadTo(30f, 40f, 70f, 30f)
                    quadTo(112f, 28f, 112f, 70f)
                    quadTo(112f, 90f, 96f, 92f)
                    lineTo(46f, 92f)
                    quadTo(36f, 92f, 34f, 86f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA85A8F)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(52f, 40f)
                    quadTo(70f, 30f, 88f, 34f)
                    lineTo(86f, 46f)
                    quadTo(70f, 42f, 54f, 50f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA85A8F)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(60f, 52f)
                    quadTo(78f, 44f, 98f, 50f)
                    lineTo(96f, 62f)
                    quadTo(78f, 56f, 62f, 64f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFD77A9B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(28f, 70f)
                    quadTo(20f, 72f, 18f, 80f)
                    quadTo(22f, 90f, 36f, 88f)
                    quadTo(42f, 80f, 40f, 72f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFE066)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22.2f, 76f)
                    arcTo(2.8f, 2.8f, 0f, true, false, 27.8f, 76f)
                    arcTo(2.8f, 2.8f, 0f, true, false, 22.2f, 76f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFFEFE6D2)), width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(20f, 84f)
                    quadTo(12f, 86f, 10f, 78f)
                }
                shape(fill = SolidColor(Color(0xFF7B3F6B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(39f, 78f)
                    horizontalLineTo(47f)
                    arcTo(5f, 5f, 0f, false, true, 52f, 83f)
                    verticalLineTo(85f)
                    arcTo(5f, 5f, 0f, false, true, 47f, 90f)
                    horizontalLineTo(39f)
                    arcTo(5f, 5f, 0f, false, true, 34f, 85f)
                    verticalLineTo(83f)
                    arcTo(5f, 5f, 0f, false, true, 39f, 78f)
                    close()
                }
            }
        }
    }

    val all: List<SpriteArt> by lazy { listOf(Biter, Spitter, Brute) }
}
