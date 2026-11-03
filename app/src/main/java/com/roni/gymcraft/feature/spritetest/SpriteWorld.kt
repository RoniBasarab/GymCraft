package com.roni.gymcraft.feature.spritetest

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.drawText
import com.roni.gymcraft.designsystem.sprites.CutoutSprites
import kotlin.math.sin

/**
 * The sprite-test battlefield: preallocated arrays, no allocation in [step] or [draw] (architecture.md §11).
 * Positions are fractions of the screen so a resize costs nothing.
 */
class SpriteWorld(private val count: Int) {

    private val x = FloatArray(count)
    private val y = FloatArray(count)
    private val speed = FloatArray(count)
    private val phase = FloatArray(count)
    private val squad = IntArray(count)
    private var lastNanos = 0L

    init {
        for (i in 0 until count) {
            x[i] = ((i * 37) % 100) / 100f
            y[i] = ((i * 53) % 100) / 100f
            speed[i] = 0.05f + (i % 5) * 0.012f
            phase[i] = ((i * 17) % 100) / 100f
            squad[i] = 3 + i % 3
        }
    }

    fun step(t: Long, width: Float, height: Float) {
        if (lastNanos == 0L) {
            lastNanos = t
            return
        }
        val dt = ((t - lastNanos) / 1_000_000_000f).coerceIn(0f, 0.1f)
        lastNanos = t
        for (i in 0 until count) {
            var next = x[i] - speed[i] * dt
            if (next < 0f) next += 1f
            x[i] = next
        }
    }

    fun draw(scope: DrawScope, sprites: CutoutSprites, t: Long) {
        val seconds = t / 1_000_000_000f
        val squadPx = scope.density * 44f
        val scale = squadPx / sprites.partSize
        val maxX = (scope.size.width - squadPx).coerceAtLeast(1f)
        val maxY = (scope.size.height - squadPx).coerceAtLeast(1f)
        val bobPx = scope.density * 2.5f
        val badgeTop = squadPx * 0.85f

        for (i in 0 until count) {
            val bob = sin(seconds * 3.1f + phase[i] * 6.283f) * bobPx
            val left = x[i] * maxX
            val top = y[i] * maxY + bob

            // Walk cycle: 1.6 s, offset by index. Recoil for 240 ms, flash for the first 80 ms.
            val cycle = (seconds / 1.6f + phase[i]) % 1f
            val recoil = if (cycle < 0.15f) sprites.partSize * 0.06f else 0f

            scope.withTransform({
                translate(left, top)
                scale(scale, scale, pivot = Offset.Zero)
            }) {
                drawImage(sprites.body)
                drawImage(sprites.stripe)
                drawImage(sprites.weapon, topLeft = Offset(recoil, 0f))
                if (cycle < 0.05f) {
                    drawImage(sprites.flash, topLeft = Offset(recoil, 0f))
                }
            }
            scope.drawText(sprites.badges[squad[i] - 1], topLeft = Offset(left, top + badgeTop))
        }
    }
}
