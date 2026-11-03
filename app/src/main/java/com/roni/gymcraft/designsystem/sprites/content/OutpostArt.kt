package com.roni.gymcraft.designsystem.sprites.content

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.roni.gymcraft.designsystem.sprites.SpriteArt
import com.roni.gymcraft.designsystem.sprites.TEAM_FILL
import com.roni.gymcraft.designsystem.sprites.sprite
import com.roni.gymcraft.engine.model.ArtId

/** The Outpost's sprites: units, buildings, and the drill sergeant. Generated from the "GymCraft Sprites" canvas by the planning agent; do not hand-edit. */
object OutpostArt {

    val Rifleman: SpriteArt by lazy {
        sprite(ArtId("outpost.rifleman"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(32f, 78.5f, 88f, 111f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(32f, 104f)
                    arcTo(28f, 7f, 0f, true, false, 88f, 104f)
                    arcTo(28f, 7f, 0f, true, false, 32f, 104f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4B5D69)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(51f, 80f)
                    horizontalLineTo(55f)
                    arcTo(4f, 4f, 0f, false, true, 59f, 84f)
                    verticalLineTo(96f)
                    arcTo(4f, 4f, 0f, false, true, 55f, 100f)
                    horizontalLineTo(51f)
                    arcTo(4f, 4f, 0f, false, true, 47f, 96f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 51f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4B5D69)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 80f)
                    horizontalLineTo(70f)
                    arcTo(4f, 4f, 0f, false, true, 74f, 84f)
                    verticalLineTo(96f)
                    arcTo(4f, 4f, 0f, false, true, 70f, 100f)
                    horizontalLineTo(66f)
                    arcTo(4f, 4f, 0f, false, true, 62f, 96f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 66f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(46f, 95f)
                    horizontalLineTo(58f)
                    arcTo(3f, 3f, 0f, false, true, 61f, 98f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 58f, 104f)
                    horizontalLineTo(46f)
                    arcTo(3f, 3f, 0f, false, true, 43f, 101f)
                    verticalLineTo(98f)
                    arcTo(3f, 3f, 0f, false, true, 46f, 95f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(63f, 95f)
                    horizontalLineTo(75f)
                    arcTo(3f, 3f, 0f, false, true, 78f, 98f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 75f, 104f)
                    horizontalLineTo(63f)
                    arcTo(3f, 3f, 0f, false, true, 60f, 101f)
                    verticalLineTo(98f)
                    arcTo(3f, 3f, 0f, false, true, 63f, 95f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(39.5f, 47.5f, 82.5f, 87.5f)) {
                shape(fill = SolidColor(Color(0xFF6F8796)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(53f, 49f)
                    horizontalLineTo(69f)
                    arcTo(12f, 12f, 0f, false, true, 81f, 61f)
                    verticalLineTo(74f)
                    arcTo(12f, 12f, 0f, false, true, 69f, 86f)
                    horizontalLineTo(53f)
                    arcTo(12f, 12f, 0f, false, true, 41f, 74f)
                    verticalLineTo(61f)
                    arcTo(12f, 12f, 0f, false, true, 53f, 49f)
                    close()
                }
            }
            layer("bob", team = true, bounds = floatArrayOf(43f, 67f, 79f, 74f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 67f)
                    horizontalLineTo(77f)
                    arcTo(2f, 2f, 0f, false, true, 79f, 69f)
                    verticalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 77f, 74f)
                    horizontalLineTo(45f)
                    arcTo(2f, 2f, 0f, false, true, 43f, 72f)
                    verticalLineTo(69f)
                    arcTo(2f, 2f, 0f, false, true, 45f, 67f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(31.5f, 16.5f, 83.5f, 75.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 67f)
                    horizontalLineTo(77f)
                    arcTo(2f, 2f, 0f, false, true, 79f, 69f)
                    verticalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 77f, 74f)
                    horizontalLineTo(45f)
                    arcTo(2f, 2f, 0f, false, true, 43f, 72f)
                    verticalLineTo(69f)
                    arcTo(2f, 2f, 0f, false, true, 45f, 67f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF6F8796)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 35f)
                    arcTo(17f, 17f, 0f, true, false, 79f, 35f)
                    arcTo(17f, 17f, 0f, true, false, 45f, 35f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(68f, 29f)
                    horizontalLineTo(77f)
                    arcTo(5f, 5f, 0f, false, true, 82f, 34f)
                    verticalLineTo(35f)
                    arcTo(5f, 5f, 0f, false, true, 77f, 40f)
                    horizontalLineTo(68f)
                    arcTo(5f, 5f, 0f, false, true, 63f, 35f)
                    verticalLineTo(34f)
                    arcTo(5f, 5f, 0f, false, true, 68f, 29f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(74.5f, 31f)
                    horizontalLineTo(76.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 78f, 32.5f)
                    verticalLineTo(32.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 76.5f, 34f)
                    horizontalLineTo(74.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 73f, 32.5f)
                    verticalLineTo(32.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 74.5f, 31f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF93ABB8)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(33f, 55f)
                    arcTo(12f, 9f, 0f, true, false, 57f, 55f)
                    arcTo(12f, 9f, 0f, true, false, 33f, 55f)
                    close()
                }
            }
            layer("recoil", team = false, bounds = floatArrayOf(49.5f, 58.5f, 103.5f, 78.5f)) {
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(54f, 60f)
                    horizontalLineTo(62f)
                    arcTo(3f, 3f, 0f, false, true, 65f, 63f)
                    verticalLineTo(69f)
                    arcTo(3f, 3f, 0f, false, true, 62f, 72f)
                    horizontalLineTo(54f)
                    arcTo(3f, 3f, 0f, false, true, 51f, 69f)
                    verticalLineTo(63f)
                    arcTo(3f, 3f, 0f, false, true, 54f, 60f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4A4F55)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(64f, 61f)
                    horizontalLineTo(100f)
                    arcTo(2f, 2f, 0f, false, true, 102f, 63f)
                    verticalLineTo(67f)
                    arcTo(2f, 2f, 0f, false, true, 100f, 69f)
                    horizontalLineTo(64f)
                    arcTo(2f, 2f, 0f, false, true, 62f, 67f)
                    verticalLineTo(63f)
                    arcTo(2f, 2f, 0f, false, true, 64f, 61f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(73f, 68f)
                    horizontalLineTo(77f)
                    arcTo(1f, 1f, 0f, false, true, 78f, 69f)
                    verticalLineTo(76f)
                    arcTo(1f, 1f, 0f, false, true, 77f, 77f)
                    horizontalLineTo(73f)
                    arcTo(1f, 1f, 0f, false, true, 72f, 76f)
                    verticalLineTo(69f)
                    arcTo(1f, 1f, 0f, false, true, 73f, 68f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF93ABB8)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(61.5f, 68f)
                    arcTo(6.5f, 6.5f, 0f, true, false, 74.5f, 68f)
                    arcTo(6.5f, 6.5f, 0f, true, false, 61.5f, 68f)
                    close()
                }
            }
            layer("flash", team = false, bounds = floatArrayOf(103f, 58f, 118f, 72f)) {
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(103f, 65f)
                    lineTo(115f, 58f)
                    lineTo(111f, 65f)
                    lineTo(118f, 72f)
                    lineTo(103f, 69f)
                    close()
                }
            }
        }
    }

    val Medic: SpriteArt by lazy {
        sprite(ArtId("outpost.medic"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(32f, 78.5f, 88f, 111f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(32f, 104f)
                    arcTo(28f, 7f, 0f, true, false, 88f, 104f)
                    arcTo(28f, 7f, 0f, true, false, 32f, 104f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA79F8C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(51f, 80f)
                    horizontalLineTo(55f)
                    arcTo(4f, 4f, 0f, false, true, 59f, 84f)
                    verticalLineTo(96f)
                    arcTo(4f, 4f, 0f, false, true, 55f, 100f)
                    horizontalLineTo(51f)
                    arcTo(4f, 4f, 0f, false, true, 47f, 96f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 51f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA79F8C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 80f)
                    horizontalLineTo(70f)
                    arcTo(4f, 4f, 0f, false, true, 74f, 84f)
                    verticalLineTo(96f)
                    arcTo(4f, 4f, 0f, false, true, 70f, 100f)
                    horizontalLineTo(66f)
                    arcTo(4f, 4f, 0f, false, true, 62f, 96f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 66f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(46f, 95f)
                    horizontalLineTo(58f)
                    arcTo(3f, 3f, 0f, false, true, 61f, 98f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 58f, 104f)
                    horizontalLineTo(46f)
                    arcTo(3f, 3f, 0f, false, true, 43f, 101f)
                    verticalLineTo(98f)
                    arcTo(3f, 3f, 0f, false, true, 46f, 95f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(63f, 95f)
                    horizontalLineTo(75f)
                    arcTo(3f, 3f, 0f, false, true, 78f, 98f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 75f, 104f)
                    horizontalLineTo(63f)
                    arcTo(3f, 3f, 0f, false, true, 60f, 101f)
                    verticalLineTo(98f)
                    arcTo(3f, 3f, 0f, false, true, 63f, 95f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(28.5f, 47.5f, 82.5f, 87.5f)) {
                shape(fill = SolidColor(Color(0xFFA79F8C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(35f, 52f)
                    horizontalLineTo(41f)
                    arcTo(5f, 5f, 0f, false, true, 46f, 57f)
                    verticalLineTo(73f)
                    arcTo(5f, 5f, 0f, false, true, 41f, 78f)
                    horizontalLineTo(35f)
                    arcTo(5f, 5f, 0f, false, true, 30f, 73f)
                    verticalLineTo(57f)
                    arcTo(5f, 5f, 0f, false, true, 35f, 52f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFD8CFBB)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(53f, 49f)
                    horizontalLineTo(69f)
                    arcTo(12f, 12f, 0f, false, true, 81f, 61f)
                    verticalLineTo(74f)
                    arcTo(12f, 12f, 0f, false, true, 69f, 86f)
                    horizontalLineTo(53f)
                    arcTo(12f, 12f, 0f, false, true, 41f, 74f)
                    verticalLineTo(61f)
                    arcTo(12f, 12f, 0f, false, true, 53f, 49f)
                    close()
                }
            }
            layer("bob", team = true, bounds = floatArrayOf(43f, 67f, 79f, 74f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 67f)
                    horizontalLineTo(77f)
                    arcTo(2f, 2f, 0f, false, true, 79f, 69f)
                    verticalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 77f, 74f)
                    horizontalLineTo(45f)
                    arcTo(2f, 2f, 0f, false, true, 43f, 72f)
                    verticalLineTo(69f)
                    arcTo(2f, 2f, 0f, false, true, 45f, 67f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(31.5f, 16.5f, 83.5f, 75.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 67f)
                    horizontalLineTo(77f)
                    arcTo(2f, 2f, 0f, false, true, 79f, 69f)
                    verticalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 77f, 74f)
                    horizontalLineTo(45f)
                    arcTo(2f, 2f, 0f, false, true, 43f, 72f)
                    verticalLineTo(69f)
                    arcTo(2f, 2f, 0f, false, true, 45f, 67f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFD8CFBB)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 35f)
                    arcTo(17f, 17f, 0f, true, false, 79f, 35f)
                    arcTo(17f, 17f, 0f, true, false, 45f, 35f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(68f, 29f)
                    horizontalLineTo(77f)
                    arcTo(5f, 5f, 0f, false, true, 82f, 34f)
                    verticalLineTo(35f)
                    arcTo(5f, 5f, 0f, false, true, 77f, 40f)
                    horizontalLineTo(68f)
                    arcTo(5f, 5f, 0f, false, true, 63f, 35f)
                    verticalLineTo(34f)
                    arcTo(5f, 5f, 0f, false, true, 68f, 29f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(74.5f, 31f)
                    horizontalLineTo(76.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 78f, 32.5f)
                    verticalLineTo(32.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 76.5f, 34f)
                    horizontalLineTo(74.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 73f, 32.5f)
                    verticalLineTo(32.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 74.5f, 31f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFEFE8D8)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(33f, 55f)
                    arcTo(12f, 9f, 0f, true, false, 57f, 55f)
                    arcTo(12f, 9f, 0f, true, false, 33f, 55f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF3F9E58)), width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(42f, 50f)
                    horizontalLineToRelative(6f)
                    verticalLineToRelative(0f)
                    moveTo(45f, 47f)
                    verticalLineToRelative(6f)
                }
            }
            layer("base", team = false, bounds = floatArrayOf(58f, 60f, 83.5f, 76f)) {
                shape(fill = SolidColor(Color(0xFF8D8F90)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 63f)
                    horizontalLineTo(78f)
                    arcTo(4f, 4f, 0f, false, true, 82f, 67f)
                    verticalLineTo(69f)
                    arcTo(4f, 4f, 0f, false, true, 78f, 73f)
                    horizontalLineTo(66f)
                    arcTo(4f, 4f, 0f, false, true, 62f, 69f)
                    verticalLineTo(67f)
                    arcTo(4f, 4f, 0f, false, true, 66f, 63f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFEFE8D8)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(59.5f, 68f)
                    arcTo(6.5f, 6.5f, 0f, true, false, 72.5f, 68f)
                    arcTo(6.5f, 6.5f, 0f, true, false, 59.5f, 68f)
                    close()
                }
            }
            layer("pulse", team = false, bounds = floatArrayOf(81f, 59f, 99f, 77f)) {
                shape(fill = SolidColor(Color(0xFF5FCF7A)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 0.35f) {
                    moveTo(81f, 68f)
                    arcTo(9f, 9f, 0f, true, false, 99f, 68f)
                    arcTo(9f, 9f, 0f, true, false, 81f, 68f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA6F0B8)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(85f, 68f)
                    arcTo(5f, 5f, 0f, true, false, 95f, 68f)
                    arcTo(5f, 5f, 0f, true, false, 85f, 68f)
                    close()
                }
            }
        }
    }

    val Rocketeer: SpriteArt by lazy {
        sprite(ArtId("outpost.rocketeer"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(32f, 78.5f, 88f, 111f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(32f, 104f)
                    arcTo(28f, 7f, 0f, true, false, 88f, 104f)
                    arcTo(28f, 7f, 0f, true, false, 32f, 104f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4B5D69)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(51f, 80f)
                    horizontalLineTo(55f)
                    arcTo(4f, 4f, 0f, false, true, 59f, 84f)
                    verticalLineTo(96f)
                    arcTo(4f, 4f, 0f, false, true, 55f, 100f)
                    horizontalLineTo(51f)
                    arcTo(4f, 4f, 0f, false, true, 47f, 96f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 51f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4B5D69)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 80f)
                    horizontalLineTo(70f)
                    arcTo(4f, 4f, 0f, false, true, 74f, 84f)
                    verticalLineTo(96f)
                    arcTo(4f, 4f, 0f, false, true, 70f, 100f)
                    horizontalLineTo(66f)
                    arcTo(4f, 4f, 0f, false, true, 62f, 96f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 66f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(46f, 95f)
                    horizontalLineTo(58f)
                    arcTo(3f, 3f, 0f, false, true, 61f, 98f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 58f, 104f)
                    horizontalLineTo(46f)
                    arcTo(3f, 3f, 0f, false, true, 43f, 101f)
                    verticalLineTo(98f)
                    arcTo(3f, 3f, 0f, false, true, 46f, 95f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(63f, 95f)
                    horizontalLineTo(75f)
                    arcTo(3f, 3f, 0f, false, true, 78f, 98f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 75f, 104f)
                    horizontalLineTo(63f)
                    arcTo(3f, 3f, 0f, false, true, 60f, 101f)
                    verticalLineTo(98f)
                    arcTo(3f, 3f, 0f, false, true, 63f, 95f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(39.5f, 47.5f, 82.5f, 87.5f)) {
                shape(fill = SolidColor(Color(0xFF5D7280)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(53f, 49f)
                    horizontalLineTo(69f)
                    arcTo(12f, 12f, 0f, false, true, 81f, 61f)
                    verticalLineTo(74f)
                    arcTo(12f, 12f, 0f, false, true, 69f, 86f)
                    horizontalLineTo(53f)
                    arcTo(12f, 12f, 0f, false, true, 41f, 74f)
                    verticalLineTo(61f)
                    arcTo(12f, 12f, 0f, false, true, 53f, 49f)
                    close()
                }
            }
            layer("bob", team = true, bounds = floatArrayOf(43f, 67f, 79f, 74f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 67f)
                    horizontalLineTo(77f)
                    arcTo(2f, 2f, 0f, false, true, 79f, 69f)
                    verticalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 77f, 74f)
                    horizontalLineTo(45f)
                    arcTo(2f, 2f, 0f, false, true, 43f, 72f)
                    verticalLineTo(69f)
                    arcTo(2f, 2f, 0f, false, true, 45f, 67f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(41.5f, 20.5f, 80.5f, 75.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(45f, 67f)
                    horizontalLineTo(77f)
                    arcTo(2f, 2f, 0f, false, true, 79f, 69f)
                    verticalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 77f, 74f)
                    horizontalLineTo(45f)
                    arcTo(2f, 2f, 0f, false, true, 43f, 72f)
                    verticalLineTo(69f)
                    arcTo(2f, 2f, 0f, false, true, 45f, 67f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF5D7280)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(44f, 38f)
                    arcTo(16f, 16f, 0f, true, false, 76f, 38f)
                    arcTo(16f, 16f, 0f, true, false, 44f, 38f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFB347)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 33f)
                    horizontalLineTo(74f)
                    arcTo(5f, 5f, 0f, false, true, 79f, 38f)
                    verticalLineTo(38f)
                    arcTo(5f, 5f, 0f, false, true, 74f, 43f)
                    horizontalLineTo(66f)
                    arcTo(5f, 5f, 0f, false, true, 61f, 38f)
                    verticalLineTo(38f)
                    arcTo(5f, 5f, 0f, false, true, 66f, 33f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(71.5f, 35f)
                    horizontalLineTo(73.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 75f, 36.5f)
                    verticalLineTo(36.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 73.5f, 38f)
                    horizontalLineTo(71.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 70f, 36.5f)
                    verticalLineTo(36.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 71.5f, 35f)
                    close()
                }
            }
            layer("recoil", team = false, bounds = floatArrayOf(20.5f, 36.5f, 95.5f, 54.5f)) {
                shape(fill = SolidColor(Color(0xFF7A7F4A)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(29f, 38f)
                    horizontalLineTo(87f)
                    arcTo(7f, 7f, 0f, false, true, 94f, 45f)
                    verticalLineTo(46f)
                    arcTo(7f, 7f, 0f, false, true, 87f, 53f)
                    horizontalLineTo(29f)
                    arcTo(7f, 7f, 0f, false, true, 22f, 46f)
                    verticalLineTo(45f)
                    arcTo(7f, 7f, 0f, false, true, 29f, 38f)
                    close()
                }
            }
            layer("recoil", team = true, bounds = floatArrayOf(64f, 38f, 72f, 53f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(64f, 38f)
                    horizontalLineTo(72f)
                    verticalLineTo(53f)
                    horizontalLineTo(64f)
                    close()
                }
            }
            layer("recoil", team = false, bounds = floatArrayOf(16.5f, 36.5f, 98.5f, 68.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(64f, 38f)
                    horizontalLineTo(72f)
                    verticalLineTo(53f)
                    horizontalLineTo(64f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(20f, 40f)
                    horizontalLineTo(24f)
                    arcTo(2f, 2f, 0f, false, true, 26f, 42f)
                    verticalLineTo(49f)
                    arcTo(2f, 2f, 0f, false, true, 24f, 51f)
                    horizontalLineTo(20f)
                    arcTo(2f, 2f, 0f, false, true, 18f, 49f)
                    verticalLineTo(42f)
                    arcTo(2f, 2f, 0f, false, true, 20f, 40f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(92f, 40f)
                    horizontalLineTo(95f)
                    arcTo(2f, 2f, 0f, false, true, 97f, 42f)
                    verticalLineTo(49f)
                    arcTo(2f, 2f, 0f, false, true, 95f, 51f)
                    horizontalLineTo(92f)
                    arcTo(2f, 2f, 0f, false, true, 90f, 49f)
                    verticalLineTo(42f)
                    arcTo(2f, 2f, 0f, false, true, 92f, 40f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF93ABB8)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(49f, 60f)
                    arcTo(7f, 7f, 0f, true, false, 63f, 60f)
                    arcTo(7f, 7f, 0f, true, false, 49f, 60f)
                    close()
                }
            }
            layer("flash", team = false, bounds = floatArrayOf(97f, 37f, 114f, 53f)) {
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(97f, 45f)
                    lineTo(110f, 37f)
                    lineTo(106f, 45f)
                    lineTo(114f, 53f)
                    lineTo(97f, 48f)
                    close()
                }
            }
            layer("backblast", team = false, bounds = floatArrayOf(9f, 40f, 19f, 50f)) {
                shape(fill = SolidColor(Color(0xFFBCAE96)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 0.7f) {
                    moveTo(9f, 45f)
                    arcTo(5f, 5f, 0f, true, false, 19f, 45f)
                    arcTo(5f, 5f, 0f, true, false, 9f, 45f)
                    close()
                }
            }
        }
    }

    val Buggy: SpriteArt by lazy {
        sprite(ArtId("outpost.buggy"), 130f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(16f, 92f, 108f, 108f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(16f, 100f)
                    arcTo(46f, 8f, 0f, true, false, 108f, 100f)
                    arcTo(46f, 8f, 0f, true, false, 16f, 100f)
                    close()
                }
            }
            layer("bounce", team = false, bounds = floatArrayOf(14.5f, 30.5f, 109.5f, 81.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF2B2F33)), width = 5f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(50f, 52f)
                    lineTo(57f, 33f)
                    lineTo(77f, 33f)
                    lineTo(84f, 50f)
                }
                shape(fill = SolidColor(Color(0xFF8A8F52)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(16f, 70f)
                    lineTo(28f, 54f)
                    lineTo(86f, 50f)
                    lineTo(108f, 62f)
                    lineTo(108f, 80f)
                    lineTo(16f, 80f)
                    close()
                }
            }
            layer("bounce", team = true, bounds = floatArrayOf(27f, 60f, 102f, 69f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(28f, 62f)
                    lineTo(100f, 60f)
                    lineTo(102f, 67f)
                    lineTo(27f, 69f)
                    close()
                }
            }
            layer("bounce", team = false, bounds = floatArrayOf(25.5f, 31.5f, 103.5f, 70.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(28f, 62f)
                    lineTo(100f, 60f)
                    lineTo(102f, 67f)
                    lineTo(27f, 69f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF6F8796)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(57f, 42f)
                    arcTo(9f, 9f, 0f, true, false, 75f, 42f)
                    arcTo(9f, 9f, 0f, true, false, 57f, 42f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(70f, 38f)
                    horizontalLineTo(74f)
                    arcTo(3f, 3f, 0f, false, true, 77f, 41f)
                    verticalLineTo(42f)
                    arcTo(3f, 3f, 0f, false, true, 74f, 45f)
                    horizontalLineTo(70f)
                    arcTo(3f, 3f, 0f, false, true, 67f, 42f)
                    verticalLineTo(41f)
                    arcTo(3f, 3f, 0f, false, true, 70f, 38f)
                    close()
                }
            }
            layer("recoil", team = false, bounds = floatArrayOf(70.5f, 42.5f, 107.5f, 51.5f)) {
                shape(fill = SolidColor(Color(0xFF4A4F55)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(74f, 44f)
                    horizontalLineTo(104f)
                    arcTo(2f, 2f, 0f, false, true, 106f, 46f)
                    verticalLineTo(48f)
                    arcTo(2f, 2f, 0f, false, true, 104f, 50f)
                    horizontalLineTo(74f)
                    arcTo(2f, 2f, 0f, false, true, 72f, 48f)
                    verticalLineTo(46f)
                    arcTo(2f, 2f, 0f, false, true, 74f, 44f)
                    close()
                }
            }
            layer("flash", team = false, bounds = floatArrayOf(106f, 42f, 119f, 52f)) {
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(106f, 47f)
                    lineTo(116f, 42f)
                    lineTo(113f, 47f)
                    lineTo(119f, 52f)
                    lineTo(106f, 50f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(19.5f, 69.5f, 104.5f, 98.5f)) {
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(21f, 84f)
                    arcTo(13f, 13f, 0f, true, false, 47f, 84f)
                    arcTo(13f, 13f, 0f, true, false, 21f, 84f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(77f, 84f)
                    arcTo(13f, 13f, 0f, true, false, 103f, 84f)
                    arcTo(13f, 13f, 0f, true, false, 77f, 84f)
                    close()
                }
            }
            layer("spin", team = false, bounds = floatArrayOf(25f, 75f, 99f, 93f)) {
                shape(fill = SolidColor(Color(0xFF8D8F90)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(29f, 84f)
                    arcTo(5f, 5f, 0f, true, false, 39f, 84f)
                    arcTo(5f, 5f, 0f, true, false, 29f, 84f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF55595E)), width = 2f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(34f, 75f)
                    verticalLineTo(93f)
                    moveTo(25f, 84f)
                    horizontalLineTo(43f)
                }
                shape(fill = SolidColor(Color(0xFF8D8F90)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(85f, 84f)
                    arcTo(5f, 5f, 0f, true, false, 95f, 84f)
                    arcTo(5f, 5f, 0f, true, false, 85f, 84f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF55595E)), width = 2f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(90f, 75f)
                    verticalLineTo(93f)
                    moveTo(81f, 84f)
                    horizontalLineTo(99f)
                }
            }
        }
    }

    val Tank: SpriteArt by lazy {
        sprite(ArtId("outpost.tank"), 130f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(8.5f, 70.5f, 112f, 111f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(12f, 102f)
                    arcTo(50f, 9f, 0f, true, false, 112f, 102f)
                    arcTo(50f, 9f, 0f, true, false, 12f, 102f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22f, 72f)
                    horizontalLineTo(98f)
                    arcTo(12f, 12f, 0f, false, true, 110f, 84f)
                    verticalLineTo(84f)
                    arcTo(12f, 12f, 0f, false, true, 98f, 96f)
                    horizontalLineTo(22f)
                    arcTo(12f, 12f, 0f, false, true, 10f, 84f)
                    verticalLineTo(84f)
                    arcTo(12f, 12f, 0f, false, true, 22f, 72f)
                    close()
                }
            }
            layer("tread", team = false, bounds = floatArrayOf(13.5f, 75.5f, 106.5f, 92.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF6A6E73)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22f, 77f)
                    horizontalLineTo(98f)
                    arcTo(7f, 7f, 0f, false, true, 105f, 84f)
                    verticalLineTo(84f)
                    arcTo(7f, 7f, 0f, false, true, 98f, 91f)
                    horizontalLineTo(22f)
                    arcTo(7f, 7f, 0f, false, true, 15f, 84f)
                    verticalLineTo(84f)
                    arcTo(7f, 7f, 0f, false, true, 22f, 77f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(12.5f, 54.5f, 109.5f, 75.5f)) {
                shape(fill = SolidColor(Color(0xFF7A7F4A)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(14f, 74f)
                    lineTo(26f, 56f)
                    lineTo(96f, 56f)
                    lineTo(108f, 74f)
                    close()
                }
            }
            layer("base", team = true, bounds = floatArrayOf(19f, 66f, 104f, 70f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22f, 66f)
                    lineTo(102f, 66f)
                    lineTo(104f, 70f)
                    lineTo(19f, 70f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(17.5f, 64.5f, 105.5f, 71.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22f, 66f)
                    lineTo(102f, 66f)
                    lineTo(104f, 70f)
                    lineTo(19f, 70f)
                    close()
                }
            }
            layer("turret", team = false, bounds = floatArrayOf(36.5f, 36.5f, 85.5f, 58.5f)) {
                shape(fill = SolidColor(Color(0xFF9CA064)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(38f, 57f)
                    quadTo(40f, 38f, 60f, 38f)
                    quadTo(82f, 38f, 84f, 57f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF5A5E35)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(54f, 42f)
                    horizontalLineTo(60f)
                    arcTo(2f, 2f, 0f, false, true, 62f, 44f)
                    verticalLineTo(46f)
                    arcTo(2f, 2f, 0f, false, true, 60f, 48f)
                    horizontalLineTo(54f)
                    arcTo(2f, 2f, 0f, false, true, 52f, 46f)
                    verticalLineTo(44f)
                    arcTo(2f, 2f, 0f, false, true, 54f, 42f)
                    close()
                }
            }
            layer("recoil", team = false, bounds = floatArrayOf(78.5f, 39.5f, 119.5f, 56.5f)) {
                shape(fill = SolidColor(Color(0xFF5A5E35)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(82f, 44f)
                    horizontalLineTo(112f)
                    arcTo(2f, 2f, 0f, false, true, 114f, 46f)
                    verticalLineTo(50f)
                    arcTo(2f, 2f, 0f, false, true, 112f, 52f)
                    horizontalLineTo(82f)
                    arcTo(2f, 2f, 0f, false, true, 80f, 50f)
                    verticalLineTo(46f)
                    arcTo(2f, 2f, 0f, false, true, 82f, 44f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4A4E2C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(112f, 41f)
                    horizontalLineTo(116f)
                    arcTo(2f, 2f, 0f, false, true, 118f, 43f)
                    verticalLineTo(53f)
                    arcTo(2f, 2f, 0f, false, true, 116f, 55f)
                    horizontalLineTo(112f)
                    arcTo(2f, 2f, 0f, false, true, 110f, 53f)
                    verticalLineTo(43f)
                    arcTo(2f, 2f, 0f, false, true, 112f, 41f)
                    close()
                }
            }
            layer("flash", team = false, bounds = floatArrayOf(118f, 40f, 130f, 57f)) {
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(118f, 48f)
                    lineTo(128f, 40f)
                    lineTo(125f, 48f)
                    lineTo(130f, 57f)
                    lineTo(118f, 51f)
                    close()
                }
            }
        }
    }

    val Walker: SpriteArt by lazy {
        sprite(ArtId("outpost.walker"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(30f, 99f, 90f, 113f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(30f, 106f)
                    arcTo(30f, 7f, 0f, true, false, 90f, 106f)
                    arcTo(30f, 7f, 0f, true, false, 30f, 106f)
                    close()
                }
            }
            layer("stepA", team = false, bounds = floatArrayOf(34.5f, 60.5f, 61.5f, 106.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 11f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(50f, 66f)
                    lineTo(40f, 86f)
                    lineTo(48f, 100f)
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF5A5E35)), width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(50f, 66f)
                    lineTo(40f, 86f)
                    lineTo(48f, 100f)
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(41f, 98f)
                    horizontalLineTo(57f)
                    arcTo(3f, 3f, 0f, false, true, 60f, 101f)
                    verticalLineTo(102f)
                    arcTo(3f, 3f, 0f, false, true, 57f, 105f)
                    horizontalLineTo(41f)
                    arcTo(3f, 3f, 0f, false, true, 38f, 102f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 41f, 98f)
                    close()
                }
            }
            layer("stepB", team = false, bounds = floatArrayOf(54.5f, 60.5f, 81.5f, 106.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 11f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(68f, 66f)
                    lineTo(60f, 86f)
                    lineTo(68f, 100f)
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF6E733F)), width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(68f, 66f)
                    lineTo(60f, 86f)
                    lineTo(68f, 100f)
                }
                shape(fill = SolidColor(Color(0xFF2B2F33)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(61f, 98f)
                    horizontalLineTo(77f)
                    arcTo(3f, 3f, 0f, false, true, 80f, 101f)
                    verticalLineTo(102f)
                    arcTo(3f, 3f, 0f, false, true, 77f, 105f)
                    horizontalLineTo(61f)
                    arcTo(3f, 3f, 0f, false, true, 58f, 102f)
                    verticalLineTo(101f)
                    arcTo(3f, 3f, 0f, false, true, 61f, 98f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(29.5f, 31.5f, 88.5f, 72.5f)) {
                shape(fill = SolidColor(Color(0xFF7A7F4A)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(31f, 52f)
                    arcTo(28f, 19f, 0f, true, false, 87f, 52f)
                    arcTo(28f, 19f, 0f, true, false, 31f, 52f)
                    close()
                }
            }
            layer("bob", team = true, bounds = floatArrayOf(34f, 58f, 84f, 66.5f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(34f, 58f)
                    quadTo(59f, 66f, 84f, 58f)
                    lineTo(84f, 62f)
                    quadTo(59f, 71f, 34f, 62f)
                    close()
                }
            }
            layer("bob", team = false, bounds = floatArrayOf(32.5f, 24.5f, 85.5f, 68f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(34f, 58f)
                    quadTo(59f, 66f, 84f, 58f)
                    lineTo(84f, 62f)
                    quadTo(59f, 71f, 34f, 62f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(57f, 46f)
                    arcTo(13f, 9f, 0f, true, false, 83f, 46f)
                    arcTo(13f, 9f, 0f, true, false, 57f, 46f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(73.5f, 42f)
                    horizontalLineTo(76.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 78f, 43.5f)
                    verticalLineTo(43.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 76.5f, 45f)
                    horizontalLineTo(73.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 72f, 43.5f)
                    verticalLineTo(43.5f)
                    arcTo(1.5f, 1.5f, 0f, false, true, 73.5f, 42f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4A4E2C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(46f, 30f)
                    horizontalLineTo(47f)
                    arcTo(2f, 2f, 0f, false, true, 49f, 32f)
                    verticalLineTo(40f)
                    arcTo(2f, 2f, 0f, false, true, 47f, 42f)
                    horizontalLineTo(46f)
                    arcTo(2f, 2f, 0f, false, true, 44f, 40f)
                    verticalLineTo(32f)
                    arcTo(2f, 2f, 0f, false, true, 46f, 30f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(43.5f, 29f)
                    arcTo(3f, 3f, 0f, true, false, 49.5f, 29f)
                    arcTo(3f, 3f, 0f, true, false, 43.5f, 29f)
                    close()
                }
            }
            layer("recoil", team = false, bounds = floatArrayOf(68.5f, 54.5f, 112.5f, 70.5f)) {
                shape(fill = SolidColor(Color(0xFF4A4F55)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(72f, 58f)
                    horizontalLineTo(106f)
                    arcTo(2f, 2f, 0f, false, true, 108f, 60f)
                    verticalLineTo(65f)
                    arcTo(2f, 2f, 0f, false, true, 106f, 67f)
                    horizontalLineTo(72f)
                    arcTo(2f, 2f, 0f, false, true, 70f, 65f)
                    verticalLineTo(60f)
                    arcTo(2f, 2f, 0f, false, true, 72f, 58f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(106f, 56f)
                    horizontalLineTo(109f)
                    arcTo(2f, 2f, 0f, false, true, 111f, 58f)
                    verticalLineTo(67f)
                    arcTo(2f, 2f, 0f, false, true, 109f, 69f)
                    horizontalLineTo(106f)
                    arcTo(2f, 2f, 0f, false, true, 104f, 67f)
                    verticalLineTo(58f)
                    arcTo(2f, 2f, 0f, false, true, 106f, 56f)
                    close()
                }
            }
            layer("flash", team = false, bounds = floatArrayOf(111f, 55f, 120f, 68.75f)) {
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(111f, 62f)
                    lineTo(120f, 55f)
                    lineTo(117f, 62f)
                    lineTo(121f, 69f)
                    lineTo(111f, 65f)
                    close()
                }
            }
        }
    }

    val HQ: SpriteArt by lazy {
        sprite(ArtId("outpost.hq"), 200f, 170f) {
            layer("base", team = false, bounds = floatArrayOf(18f, 52.5f, 182f, 160f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(18f, 146f)
                    arcTo(82f, 14f, 0f, true, false, 182f, 146f)
                    arcTo(82f, 14f, 0f, true, false, 18f, 146f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF8A8478)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22f, 120f)
                    lineTo(44f, 92f)
                    lineTo(156f, 92f)
                    lineTo(178f, 120f)
                    lineTo(156f, 144f)
                    lineTo(44f, 144f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF6C675D)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(22f, 120f)
                    lineTo(44f, 144f)
                    lineTo(156f, 144f)
                    lineTo(178f, 120f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF6F8796)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(60f, 96f)
                    quadTo(62f, 56f, 100f, 54f)
                    quadTo(138f, 56f, 140f, 96f)
                    close()
                }
            }
            layer("glow", team = false, bounds = floatArrayOf(63.5f, 75.5f, 136.5f, 91.5f)) {
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(66f, 82f)
                    quadTo(100f, 72f, 134f, 82f)
                    lineTo(135f, 90f)
                    quadTo(100f, 80f, 65f, 90f)
                    close()
                }
            }
            layer("base", team = true, bounds = floatArrayOf(60f, 96f, 140f, 102f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(60f, 96f)
                    lineTo(140f, 96f)
                    lineTo(140f, 102f)
                    lineTo(60f, 102f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(58.5f, 94.5f, 141.5f, 145.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(60f, 96f)
                    lineTo(140f, 96f)
                    lineTo(140f, 102f)
                    lineTo(60f, 102f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(89f, 112f)
                    horizontalLineTo(111f)
                    arcTo(3f, 3f, 0f, false, true, 114f, 115f)
                    verticalLineTo(141f)
                    arcTo(3f, 3f, 0f, false, true, 111f, 144f)
                    horizontalLineTo(89f)
                    arcTo(3f, 3f, 0f, false, true, 86f, 141f)
                    verticalLineTo(115f)
                    arcTo(3f, 3f, 0f, false, true, 89f, 112f)
                    close()
                }
            }
            layer("base", team = true, bounds = floatArrayOf(92f, 117f, 108f, 121f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(94f, 117f)
                    horizontalLineTo(106f)
                    arcTo(2f, 2f, 0f, false, true, 108f, 119f)
                    verticalLineTo(119f)
                    arcTo(2f, 2f, 0f, false, true, 106f, 121f)
                    horizontalLineTo(94f)
                    arcTo(2f, 2f, 0f, false, true, 92f, 119f)
                    verticalLineTo(119f)
                    arcTo(2f, 2f, 0f, false, true, 94f, 117f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(116.5f, 17.5f, 133.5f, 58.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(118f, 58f)
                    lineTo(132f, 18f)
                }
            }
            layer("blink", team = false, bounds = floatArrayOf(126f, 11f, 138f, 23f)) {
                shape(fill = SolidColor(Color(0xFFFF5C5C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(127.5f, 17f)
                    arcTo(4.5f, 4.5f, 0f, true, false, 136.5f, 17f)
                    arcTo(4.5f, 4.5f, 0f, true, false, 127.5f, 17f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(54.5f, 46f, 57.5f, 96f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(56f, 96f)
                    lineTo(56f, 46f)
                }
            }
            layer("wave", team = true, bounds = floatArrayOf(56f, 46f, 84f, 62f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(56f, 46f)
                    lineTo(84f, 50f)
                    lineTo(56f, 62f)
                    close()
                }
            }
            layer("wave", team = false, bounds = floatArrayOf(54.5f, 44.5f, 85.5f, 63.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(56f, 46f)
                    lineTo(84f, 50f)
                    lineTo(56f, 62f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(28.5f, 130.5f, 171.5f, 148.5f)) {
                shape(fill = SolidColor(Color(0xFFA2916F)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(30f, 138f)
                    arcTo(10f, 6f, 0f, true, false, 50f, 138f)
                    arcTo(10f, 6f, 0f, true, false, 30f, 138f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFB09F7C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(46f, 141f)
                    arcTo(10f, 6f, 0f, true, false, 66f, 141f)
                    arcTo(10f, 6f, 0f, true, false, 46f, 141f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA2916F)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(134f, 141f)
                    arcTo(10f, 6f, 0f, true, false, 154f, 141f)
                    arcTo(10f, 6f, 0f, true, false, 134f, 141f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFB09F7C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(150f, 138f)
                    arcTo(10f, 6f, 0f, true, false, 170f, 138f)
                    arcTo(10f, 6f, 0f, true, false, 150f, 138f)
                    close()
                }
            }
        }
    }

    val Barracks: SpriteArt by lazy {
        sprite(ArtId("outpost.barracks"), 200f, 160f) {
            layer("base", team = false, bounds = floatArrayOf(20f, 48.5f, 180f, 147f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(20f, 134f)
                    arcTo(80f, 13f, 0f, true, false, 180f, 134f)
                    arcTo(80f, 13f, 0f, true, false, 20f, 134f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF8A8F52)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(26f, 128f)
                    lineTo(26f, 92f)
                    quadTo(26f, 50f, 100f, 50f)
                    quadTo(174f, 50f, 174f, 92f)
                    lineTo(174f, 128f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF5A5E35)), width = 2.5f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(60f, 56f)
                    quadTo(58f, 90f, 60f, 128f)
                    moveTo(100f, 50f)
                    lineTo(100f, 128f)
                    moveTo(140f, 56f)
                    quadTo(142f, 90f, 140f, 128f)
                }
            }
            layer("base", team = true, bounds = floatArrayOf(26f, 104f, 174f, 111f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(26f, 104f)
                    lineTo(174f, 104f)
                    lineTo(174f, 111f)
                    lineTo(26f, 111f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(24.5f, 80.5f, 175.5f, 129.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(26f, 104f)
                    lineTo(174f, 104f)
                    lineTo(174f, 111f)
                    lineTo(26f, 111f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF3B3F44)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(80f, 128f)
                    lineTo(80f, 96f)
                    quadTo(80f, 82f, 100f, 82f)
                    quadTo(120f, 82f, 120f, 96f)
                    lineTo(120f, 128f)
                    close()
                }
            }
            layer("glow", team = false, bounds = floatArrayOf(94f, 88f, 106f, 92f)) {
                shape(fill = SolidColor(Color(0xFFFFD65A)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(96f, 88f)
                    horizontalLineTo(104f)
                    arcTo(2f, 2f, 0f, false, true, 106f, 90f)
                    verticalLineTo(90f)
                    arcTo(2f, 2f, 0f, false, true, 104f, 92f)
                    horizontalLineTo(96f)
                    arcTo(2f, 2f, 0f, false, true, 94f, 90f)
                    verticalLineTo(90f)
                    arcTo(2f, 2f, 0f, false, true, 96f, 88f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(21.5f, 38.5f, 178.5f, 137.5f)) {
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(137f, 80f)
                    horizontalLineTo(153f)
                    arcTo(3f, 3f, 0f, false, true, 156f, 83f)
                    verticalLineTo(91f)
                    arcTo(3f, 3f, 0f, false, true, 153f, 94f)
                    horizontalLineTo(137f)
                    arcTo(3f, 3f, 0f, false, true, 134f, 91f)
                    verticalLineTo(83f)
                    arcTo(3f, 3f, 0f, false, true, 137f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(47f, 80f)
                    horizontalLineTo(63f)
                    arcTo(3f, 3f, 0f, false, true, 66f, 83f)
                    verticalLineTo(91f)
                    arcTo(3f, 3f, 0f, false, true, 63f, 94f)
                    horizontalLineTo(47f)
                    arcTo(3f, 3f, 0f, false, true, 44f, 91f)
                    verticalLineTo(83f)
                    arcTo(3f, 3f, 0f, false, true, 47f, 80f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA2916F)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(23f, 130f)
                    arcTo(11f, 6f, 0f, true, false, 45f, 130f)
                    arcTo(11f, 6f, 0f, true, false, 23f, 130f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFB09F7C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(155f, 130f)
                    arcTo(11f, 6f, 0f, true, false, 177f, 130f)
                    arcTo(11f, 6f, 0f, true, false, 155f, 130f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF5A5E35)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(152f, 40f)
                    horizontalLineTo(158f)
                    arcTo(2f, 2f, 0f, false, true, 160f, 42f)
                    verticalLineTo(54f)
                    arcTo(2f, 2f, 0f, false, true, 158f, 56f)
                    horizontalLineTo(152f)
                    arcTo(2f, 2f, 0f, false, true, 150f, 54f)
                    verticalLineTo(42f)
                    arcTo(2f, 2f, 0f, false, true, 152f, 40f)
                    close()
                }
            }
            layer("smoke", team = false, bounds = floatArrayOf(149f, 28f, 161f, 40f)) {
                shape(fill = SolidColor(Color(0xFFBCAE96)), stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 0.6f) {
                    moveTo(149f, 34f)
                    arcTo(6f, 6f, 0f, true, false, 161f, 34f)
                    arcTo(6f, 6f, 0f, true, false, 149f, 34f)
                    close()
                }
            }
        }
    }

    val MotorPool: SpriteArt by lazy {
        sprite(ArtId("outpost.motor_pool"), 200f, 160f) {
            layer("base", team = false, bounds = floatArrayOf(18f, 54.5f, 182f, 149f)) {
                shape(fill = SolidColor(Color(0xFF000000)), stroke = null, width = 1f, cap = StrokeCap.Butt, join = StrokeJoin.Miter, alpha = 0.35f) {
                    moveTo(18f, 136f)
                    arcTo(82f, 13f, 0f, true, false, 182f, 136f)
                    arcTo(82f, 13f, 0f, true, false, 18f, 136f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF8A8478)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(20f, 130f)
                    lineTo(20f, 66f)
                    lineTo(120f, 56f)
                    lineTo(180f, 66f)
                    lineTo(180f, 130f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFA39D8F)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(20f, 66f)
                    lineTo(120f, 56f)
                    lineTo(180f, 66f)
                    lineTo(120f, 70f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF4A4F55)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(42f, 78f)
                    horizontalLineTo(134f)
                    arcTo(2f, 2f, 0f, false, true, 136f, 80f)
                    verticalLineTo(128f)
                    arcTo(2f, 2f, 0f, false, true, 134f, 130f)
                    horizontalLineTo(42f)
                    arcTo(2f, 2f, 0f, false, true, 40f, 128f)
                    verticalLineTo(80f)
                    arcTo(2f, 2f, 0f, false, true, 42f, 78f)
                    close()
                }
            }
            layer("base", team = true, bounds = floatArrayOf(40f, 78f, 136f, 130f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(40f, 78f)
                    lineTo(56f, 78f)
                    lineTo(40f, 94f)
                    close()
                    moveTo(68f, 78f)
                    lineTo(84f, 78f)
                    lineTo(40f, 122f)
                    lineTo(40f, 106f)
                    close()
                    moveTo(96f, 78f)
                    lineTo(112f, 78f)
                    lineTo(64f, 130f)
                    lineTo(48f, 130f)
                    close()
                    moveTo(124f, 78f)
                    lineTo(136f, 78f)
                    lineTo(136f, 86f)
                    lineTo(92f, 130f)
                    lineTo(76f, 130f)
                    close()
                    moveTo(136f, 106f)
                    lineTo(136f, 122f)
                    lineTo(128f, 130f)
                    lineTo(112f, 130f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(38.5f, 19.5f, 196f, 131.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(42f, 78f)
                    horizontalLineTo(134f)
                    arcTo(2f, 2f, 0f, false, true, 136f, 80f)
                    verticalLineTo(128f)
                    arcTo(2f, 2f, 0f, false, true, 134f, 130f)
                    horizontalLineTo(42f)
                    arcTo(2f, 2f, 0f, false, true, 40f, 128f)
                    verticalLineTo(80f)
                    arcTo(2f, 2f, 0f, false, true, 42f, 78f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF7FD4FF)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(151f, 86f)
                    horizontalLineTo(167f)
                    arcTo(3f, 3f, 0f, false, true, 170f, 89f)
                    verticalLineTo(99f)
                    arcTo(3f, 3f, 0f, false, true, 167f, 102f)
                    horizontalLineTo(151f)
                    arcTo(3f, 3f, 0f, false, true, 148f, 99f)
                    verticalLineTo(89f)
                    arcTo(3f, 3f, 0f, false, true, 151f, 86f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF5A5E35)), width = 5f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(150f, 56f)
                    lineTo(150f, 22f)
                    lineTo(196f, 22f)
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 1.5f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(150f, 56f)
                    lineTo(150f, 22f)
                    lineTo(196f, 22f)
                }
            }
            layer("swing", team = false, bounds = floatArrayOf(181.5f, 22f, 194.5f, 50.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 2f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(188f, 22f)
                    lineTo(188f, 44f)
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(183f, 44f)
                    quadTo(188f, 54f, 193f, 44f)
                }
            }
            layer("base", team = false, bounds = floatArrayOf(154.5f, 108.5f, 171.5f, 131.5f)) {
                shape(fill = SolidColor(Color(0xFFB5523B)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(159f, 110f)
                    horizontalLineTo(167f)
                    arcTo(3f, 3f, 0f, false, true, 170f, 113f)
                    verticalLineTo(127f)
                    arcTo(3f, 3f, 0f, false, true, 167f, 130f)
                    horizontalLineTo(159f)
                    arcTo(3f, 3f, 0f, false, true, 156f, 127f)
                    verticalLineTo(113f)
                    arcTo(3f, 3f, 0f, false, true, 159f, 110f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF15110D)), width = 2f, cap = StrokeCap.Butt, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(156f, 117f)
                    lineTo(170f, 117f)
                }
            }
        }
    }

    val Sergeant: SpriteArt by lazy {
        sprite(ArtId("outpost.sergeant"), 120f, 120f) {
            layer("base", team = false, bounds = floatArrayOf(12.5f, 82.5f, 107.5f, 119.5f)) {
                shape(fill = SolidColor(Color(0xFF6F8796)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(14f, 118f)
                    lineTo(20f, 92f)
                    quadTo(60f, 76f, 100f, 92f)
                    lineTo(106f, 118f)
                    close()
                }
            }
            layer("base", team = true, bounds = floatArrayOf(17f, 98f, 103f, 110f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(18f, 104f)
                    quadTo(60f, 92f, 102f, 104f)
                    lineTo(103f, 110f)
                    quadTo(60f, 98f, 17f, 110f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(15.5f, 12.5f, 104.5f, 111.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(18f, 104f)
                    quadTo(60f, 92f, 102f, 104f)
                    lineTo(103f, 110f)
                    quadTo(60f, 98f, 17f, 110f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFD9A77A)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(53f, 74f)
                    horizontalLineTo(67f)
                    arcTo(4f, 4f, 0f, false, true, 71f, 78f)
                    verticalLineTo(84f)
                    arcTo(4f, 4f, 0f, false, true, 67f, 88f)
                    horizontalLineTo(53f)
                    arcTo(4f, 4f, 0f, false, true, 49f, 84f)
                    verticalLineTo(78f)
                    arcTo(4f, 4f, 0f, false, true, 53f, 74f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFFE9B98C)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(34f, 50f)
                    quadTo(34f, 30f, 60f, 30f)
                    quadTo(86f, 30f, 86f, 50f)
                    lineTo(84f, 68f)
                    quadTo(80f, 84f, 60f, 86f)
                    quadTo(40f, 84f, 36f, 68f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF5D7280)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(28f, 46f)
                    quadTo(28f, 16f, 60f, 14f)
                    quadTo(92f, 16f, 92f, 46f)
                    lineTo(92f, 50f)
                    lineTo(28f, 50f)
                    close()
                }
            }
            layer("base", team = true, bounds = floatArrayOf(28f, 44f, 92f, 51f)) {
                shape(fill = TEAM_FILL, stroke = null, width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(28f, 44f)
                    lineTo(92f, 44f)
                    lineTo(92f, 51f)
                    lineTo(28f, 51f)
                    close()
                }
            }
            layer("base", team = false, bounds = floatArrayOf(26.5f, 42.5f, 93.5f, 81.5f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(28f, 44f)
                    lineTo(92f, 44f)
                    lineTo(92f, 51f)
                    lineTo(28f, 51f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(40f, 58f)
                    lineTo(54f, 61f)
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(80f, 58f)
                    lineTo(66f, 61f)
                }
                shape(fill = SolidColor(Color(0xFF15110D)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(44.5f, 65f)
                    arcTo(3.5f, 2.5f, 0f, true, false, 51.5f, 65f)
                    arcTo(3.5f, 2.5f, 0f, true, false, 44.5f, 65f)
                    close()
                }
                shape(fill = SolidColor(Color(0xFF15110D)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(68.5f, 65f)
                    arcTo(3.5f, 2.5f, 0f, true, false, 75.5f, 65f)
                    arcTo(3.5f, 2.5f, 0f, true, false, 68.5f, 65f)
                    close()
                }
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(60f, 64f)
                    lineTo(57f, 73f)
                    lineTo(62f, 74f)
                }
                shape(fill = SolidColor(Color(0xFF4A3020)), stroke = SolidColor(Color(0xFF15110D)), width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(44f, 79f)
                    quadTo(52f, 73f, 60f, 76f)
                    quadTo(68f, 73f, 76f, 79f)
                    quadTo(68f, 81f, 60f, 79f)
                    quadTo(52f, 81f, 44f, 79f)
                    close()
                }
            }
            layer("talk", team = false, bounds = floatArrayOf(50.75f, 81.75f, 69.25f, 84.25f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFF15110D)), width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(52f, 83f)
                    lineTo(68f, 83f)
                }
            }
            layer("base", team = false, bounds = floatArrayOf(75f, 61f, 83f, 73f)) {
                shape(fill = null, stroke = SolidColor(Color(0xFFC98D6A)), width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, alpha = 1f) {
                    moveTo(76f, 62f)
                    lineTo(82f, 72f)
                }
            }
        }
    }

    val all: List<SpriteArt> by lazy { listOf(Rifleman, Medic, Rocketeer, Buggy, Tank, Walker, HQ, Barracks, MotorPool, Sergeant) }
}
