package com.roni.gymcraft.designsystem.sprites

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.roni.gymcraft.designsystem.sprites.content.SpriteIndex
import com.roni.gymcraft.engine.model.ArtId
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sin

/** A sprite ready to draw: its art plus one painter per layer, made once ([rememberSprite]). */
@Stable
class Sprite internal constructor(val art: SpriteArt, internal val painters: List<VectorPainter>)

@Composable
fun rememberSprite(art: SpriteArt): Sprite {
    val painters = art.layers.map { rememberVectorPainter(it.image) }
    return remember(art) { Sprite(art, painters) }
}

/** The sprite for [id], or null when there's no art for it (draw [drawPlaceholder] instead). Call it inside `key(id)` in a loop. */
@Composable
fun rememberSprite(id: ArtId): Sprite? {
    val art = SpriteIndex.of(id) ?: return null
    return rememberSprite(art)
}

/**
 * Seconds for idle animations, updated at most [fps] times a second (architecture.md §11: read it only while
 * drawing, so a tick redraws and never recomposes). It stops with the composition and while the app is in the
 * background (no frames).
 */
@Composable
fun rememberIdleClock(fps: Int = 30): State<Float> {
    val seconds = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(fps) {
        val step = 1_000_000_000L / fps
        var start = -1L
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (start < 0) start = now
                if (now - last >= step) {
                    last = now
                    seconds.floatValue = ((now - start) % 3_600_000_000_000L) / 1_000_000_000f
                }
            }
        }
    }
    return seconds
}

/**
 * Draws [sprite] standing with its bottom center at ([x], [bottom]), [height] px tall, idling at time [t] (seconds;
 * [phase] shifts it so sprites don't move in lockstep). Each layer moves by its part (game-design.md §10, §12):
 * - bob, bounce, breathe, glow, pulse, blink, wave (with a gust now and then), swing, smoke, turret, radar, flicker, tick;
 * - door / garage open for a moment every few seconds; peek (the recruit behind the door) steps up a little meanwhile;
 * - poseA / poseB and punchA / punchB alternate (the yard, the drill recruit); with [hold], pose B is held and shakes;
 * - talk moves only while [talking]; cheer shows only while [cheer] (0..1) runs, and the whole sprite hops once.
 * Battle-only parts (flash, backblast, spit) are hidden; anything else stands still. No allocation: call it every frame.
 */
fun DrawScope.drawIdleSprite(
    sprite: Sprite,
    x: Float,
    bottom: Float,
    height: Float,
    t: Float,
    phase: Float = 0f,
    alpha: Float = 1f,
    talking: Boolean = false,
    hold: Boolean = false,
    cheer: Float = 0f,
) {
    val art = sprite.art
    val s = height / art.height
    val w = art.width * s
    val time = t + phase
    val hop = if (cheer > 0f && cheer < 1f) -sin(cheer * PI.toFloat()) * 8f * s else 0f
    val poseA = !hold && time % 1.2f < 0.6f
    val punchA = time % 0.7f < 0.35f
    translate(x - w / 2f, bottom - height + hop) {
        for (i in art.layers.indices) {
            val layer = art.layers[i]
            val painter = sprite.painters[i]
            when (layer.part) {
                "flash", "backblast", "spit" -> Unit
                "bob" -> translate(0f, wave(time, 0.6f) * 1.2f * s) { paint(painter, w, height, alpha) }
                "bounce" -> translate(0f, -abs(wave(time, 0.4f)) * 1.5f * s) { paint(painter, w, height, alpha) }
                "breathe" -> scale(1f, 1f + 0.03f * wave(time, 0.4f), Offset(layer.centerX * s, layer.bounds[3] * s)) {
                    paint(painter, w, height, alpha)
                }
                "glow" -> paint(painter, w, height, alpha * (0.7f + 0.3f * wave(time, 0.8f)))
                "pulse" -> paint(painter, w, height, alpha * (0.6f + 0.4f * (0.5f + 0.5f * wave(time, 0.9f))))
                "blink" -> if (time % 1.6f < 0.9f) paint(painter, w, height, alpha)
                "flicker" -> paint(painter, w, height, alpha * flicker(time, layer.bounds[0]))
                "wave" -> rotate(5f * wave(time, 0.5f) + 7f * gust(time) * wave(time, 1.7f), Offset(layer.bounds[0] * s, layer.centerY * s)) {
                    paint(painter, w, height, alpha)
                }
                "swing" -> rotate(7f * wave(time, 0.35f), Offset(layer.centerX * s, layer.bounds[1] * s)) {
                    paint(painter, w, height, alpha)
                }
                "turret" -> rotate(3f * wave(time, 0.2f), Offset(layer.centerX * s, layer.centerY * s)) {
                    paint(painter, w, height, alpha)
                }
                "radar" -> scale(cos(time * TWO_PI * 0.33f), 1f, Offset(layer.centerX * s, layer.centerY * s)) {
                    paint(painter, w, height, alpha)
                }
                "tick" -> rotate(floor(time % 12f) * 30f, Offset(layer.centerX * s, layer.centerY * s)) {
                    paint(painter, w, height, alpha)
                }
                "smoke" -> {
                    val c = (time * 0.4f) % 1f
                    translate(0f, -c * 14f * s) {
                        scale(1f + c * 0.6f, 1f + c * 0.6f, Offset(layer.centerX * s, layer.centerY * s)) {
                            paint(painter, w, height, alpha * (1f - c))
                        }
                    }
                }
                "door" -> scale(1f - 0.88f * opening(time, 8f), 1f, Offset(layer.bounds[0] * s, layer.centerY * s)) {
                    paint(painter, w, height, alpha)
                }
                "peek" -> translate(0f, -opening(time, 8f) * 3f * s) { paint(painter, w, height, alpha) }   // inside the doorway: hidden while closed
                "garage" -> scale(1f, 1f - 0.88f * opening(time, 9f), Offset(layer.centerX * s, layer.bounds[1] * s)) {
                    paint(painter, w, height, alpha)
                }
                "poseA" -> if (poseA) paint(painter, w, height, alpha)
                "poseB" -> if (hold) {
                    translate(sin(time * 70f) * 0.6f * s, 0f) { paint(painter, w, height, alpha) }
                } else if (!poseA) {
                    paint(painter, w, height, alpha)
                }
                "punchA" -> if (punchA) paint(painter, w, height, alpha)
                "punchB" -> if (!punchA) paint(painter, w, height, alpha)
                "talk" -> translate(0f, if (talking && wave(time, 4f) > 0f) 1.5f * s else 0f) { paint(painter, w, height, alpha) }
                "cheer" -> if (cheer > 0f && cheer < 1f) paint(painter, w, height, alpha)
                else -> paint(painter, w, height, alpha)
            }
        }
    }
}

/** Where art is missing: a grey block, never a crash (architecture.md §4). */
fun DrawScope.drawPlaceholder(x: Float, bottom: Float, height: Float, alpha: Float = 1f) {
    val w = height * 0.8f
    drawRoundRect(
        color = Color(0xFF5A5148),
        topLeft = Offset(x - w / 2f, bottom - height),
        size = Size(w, height),
        cornerRadius = CornerRadius(height * 0.12f),
        alpha = alpha,
    )
}

/** A single sprite as a composable (sheet rows, the sergeant's portrait, the drill recruit), idling. */
@Composable
fun SpriteImage(id: ArtId, modifier: Modifier = Modifier, talking: Boolean = false, hold: Boolean = false) {
    val sprite = rememberSprite(id)
    val clock = rememberIdleClock()
    Canvas(modifier) {
        val t = clock.value
        val h = if (sprite == null) size.height * 0.8f else minOf(size.height, size.width * sprite.art.height / sprite.art.width)
        if (sprite == null) drawPlaceholder(size.width / 2f, size.height, h)
        else drawIdleSprite(sprite, size.width / 2f, size.height, h, t, talking = talking, hold = hold)
    }
}

/** A still sprite (an icon: the protein tub on prices and the top bar). No clock, so it draws once. */
@Composable
fun SpriteIcon(id: ArtId, modifier: Modifier = Modifier) {
    val sprite = rememberSprite(id)
    Canvas(modifier) {
        val h = if (sprite == null) size.height * 0.8f else minOf(size.height, size.width * sprite.art.height / sprite.art.width)
        if (sprite == null) drawPlaceholder(size.width / 2f, size.height, h)
        else drawIdleSprite(sprite, size.width / 2f, size.height, h, 0f)
    }
}

private const val TWO_PI = 6.2831855f

private fun wave(t: Float, hz: Float): Float = sin(t * TWO_PI * hz)

/** 0 = closed, 1 = open: every [period] seconds a door opens (0.4 s), stays open (1.4 s) and closes (0.4 s). */
private fun opening(t: Float, period: Float): Float {
    val c = t % period
    val o = when {
        c < 0.4f -> c / 0.4f
        c < 1.8f -> 1f
        c < 2.2f -> 1f - (c - 1.8f) / 0.4f
        else -> 0f
    }
    return o * o * (3f - 2f * o)
}

/** A flag gust: 0 most of the time, rising to 1 for about a second every 6 s. */
private fun gust(t: Float): Float {
    val g = max(0f, sin(t * TWO_PI / 6f))
    return g * g * g * g
}

/** A window light: on, with a short dim now and then (different per window, by its [seed] position). */
private fun flicker(t: Float, seed: Float): Float {
    val k = floor(t * 10f).toLong() + (seed * 31f).toLong()
    val h = (k * 2_654_435_761L) ushr 9
    return if (h % 29L == 0L || h % 31L == 0L) 0.35f else 1f
}

private fun DrawScope.paint(painter: VectorPainter, w: Float, h: Float, alpha: Float) {
    with(painter) { draw(Size(w, h), alpha = alpha) }
}
