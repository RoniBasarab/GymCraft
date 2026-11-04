package com.roni.gymcraft.feature.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import com.roni.gymcraft.designsystem.sprites.Sprite
import com.roni.gymcraft.designsystem.sprites.content.SpriteIndex
import com.roni.gymcraft.designsystem.sprites.drawIdleSprite
import com.roni.gymcraft.designsystem.sprites.drawPlaceholder
import com.roni.gymcraft.designsystem.sprites.rememberIdleClock
import com.roni.gymcraft.designsystem.sprites.rememberSprite

/** The scene's sky and ground (Home paints the strips above and below the scene in the same colors). */
val BaseSky = Color(0xFF28231D)
val BaseGround = Color(0xFF3A3226)

/**
 * The base, filling the space it's given (game-design.md §12): the production buildings on the back row with
 * "Barracks 1" above each, the HQ and the training yard in front, the army at the bottom with one count badge above
 * each squad and its wounded below. Tap a building to open its sheet. A new [cheers] makes the army hop and the
 * infantry raise a fist once. Idle animation at 30 fps, read only while drawing; labels are measured once per change.
 */
@Composable
fun BaseView(game: GameUi, cheers: Int, onAction: (HomeAction) -> Unit, modifier: Modifier = Modifier) {
    val hq = rememberSprite(game.hq)
    val yard = key(game.race) { rememberSprite(SpriteIndex.yard(game.race)) }
    val buildings = game.buildings.map { b -> key(b.art) { rememberSprite(b.art) } }
    val army = game.army.map { q -> key(q.art) { rememberSprite(q.art) } }
    val colors = MaterialTheme.colorScheme
    val measurer = rememberTextMeasurer()
    val label = MaterialTheme.typography.labelLarge.copy(color = colors.onSurface, fontWeight = FontWeight.Bold)
    val muted = MaterialTheme.typography.labelMedium.copy(color = colors.onSurfaceVariant)
    val occupied = MaterialTheme.typography.labelLarge.copy(color = colors.error, fontWeight = FontWeight.Bold)
    val buildingLabels = remember(game.buildings, label) { game.buildings.map { measurer.measure("${it.name} ${it.level}", label) } }
    val occupiedLabel = remember(occupied) { measurer.measure("OCCUPIED", occupied) }
    val badges = remember(game.army, label) { game.army.map { q -> measurer.measure("×${q.ready}", label) } }
    val hurt = remember(game.army, muted) { game.army.map { q -> if (q.wounded > 0) measurer.measure("+${q.wounded} hurt", muted) else null } }
    val emptyLabel = remember(muted) { measurer.measure("No troops yet. Tap a building to train.", muted) }
    val hits = remember(game.buildings.size) { FloatArray(game.buildings.size * 4) }
    val ids by rememberUpdatedState(game.buildings.map { it.id })
    val action by rememberUpdatedState(onAction)
    val clock = rememberIdleClock()

    // The cheer: an animation, so it's local (architecture.md §5). Only a new "I went" starts it, not a return to Home.
    val cheer = remember { Animatable(1f) }
    var cheered by rememberSaveable { mutableIntStateOf(cheers) }
    LaunchedEffect(cheers) {
        if (cheers > cheered) {
            cheered = cheers
            cheer.snapTo(0f)
            cheer.animateTo(1f, tween(1_400, easing = LinearEasing))
        }
    }

    Canvas(
        modifier
            .pointerInput(hits) {
                detectTapGestures { p ->
                    for (i in ids.indices) {
                        if (p.x >= hits[i * 4] && p.y >= hits[i * 4 + 1] && p.x <= hits[i * 4 + 2] && p.y <= hits[i * 4 + 3]) {
                            action(HomeAction.OpenBuilding(ids[i]))
                            break
                        }
                    }
                }
            }
            .testTag("base_view"),
    ) {
        val t = clock.value
        val w = size.width
        val h = size.height
        drawRect(BaseSky)
        drawRect(BaseGround, topLeft = Offset(0f, h * 0.3f))

        // Back row: the production buildings, evenly across.
        val n = buildings.size
        val slot = if (n == 0) 0f else w / n
        for (i in buildings.indices) {
            val b = game.buildings.getOrNull(i) ?: continue
            val sprite = buildings[i]
            val ratio = sprite?.let { it.art.height / it.art.width } ?: 0.8f
            val bh = minOf(h * 0.34f, slot * 0.86f * ratio)
            val bw = bh / ratio
            val cx = slot * (i + 0.5f)
            val bottom = h * 0.44f
            val a = if (b.occupied) 0.45f else 1f
            if (sprite != null) drawIdleSprite(sprite, cx, bottom, bh, t, phase = i * 3.1f, alpha = a) else drawPlaceholder(cx, bottom, bh, a)
            val text = buildingLabels.getOrNull(i)
            val labelH = text?.size?.height ?: 0
            if (text != null) drawText(text, topLeft = Offset(cx - text.size.width / 2f, bottom - bh - labelH))
            if (b.occupied) {
                drawText(occupiedLabel, topLeft = Offset(cx - occupiedLabel.size.width / 2f, bottom - bh / 2f - occupiedLabel.size.height / 2f))
            }
            hits[i * 4] = cx - bw / 2f
            hits[i * 4 + 1] = bottom - bh - labelH
            hits[i * 4 + 2] = cx + bw / 2f
            hits[i * 4 + 3] = bottom
        }

        // Front: the HQ, and the training yard beside it.
        drawFront(hq, w * 0.3f, h * 0.76f, minOf(h * 0.38f, w * 0.52f * 170f / 200f), t)
        drawFront(yard, w * 0.76f, h * 0.74f, minOf(h * 0.32f, w * 0.44f * 150f / 200f), t + 0.4f)

        // The army, one sprite per unit type (game-design.md §6).
        if (army.isEmpty()) {
            drawText(emptyLabel, topLeft = Offset((w - emptyLabel.size.width) / 2f, h * 0.86f))
        } else {
            val unitSlot = w * 0.94f / maxOf(army.size, 5)
            val uh = minOf(h * 0.17f, unitSlot * 0.95f)
            val bottom = h * 0.93f
            val wave = 0.06f
            for (i in army.indices) {
                val q = game.army.getOrNull(i) ?: continue
                val sprite = army[i]
                val cx = w * 0.03f + unitSlot * (i + 0.5f)
                val a = if (q.ready == 0) 0.4f else 1f
                val c = (cheer.value * (1f + wave * (army.size - 1)) - i * wave).coerceIn(0f, 1f)
                if (sprite != null) drawIdleSprite(sprite, cx, bottom, uh, t, phase = i * 1.3f, alpha = a, cheer = c)
                else drawPlaceholder(cx, bottom, uh, a)
                val badge = badges.getOrNull(i) ?: continue
                drawText(badge, topLeft = Offset(cx - badge.size.width / 2f, bottom - uh - badge.size.height))
                val wounded = hurt.getOrNull(i) ?: continue
                drawText(wounded, topLeft = Offset(cx - wounded.size.width / 2f, bottom + 1f))
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFront(sprite: Sprite?, cx: Float, bottom: Float, height: Float, t: Float) {
    if (sprite != null) drawIdleSprite(sprite, cx, bottom, height, t) else drawPlaceholder(cx, bottom, height)
}
