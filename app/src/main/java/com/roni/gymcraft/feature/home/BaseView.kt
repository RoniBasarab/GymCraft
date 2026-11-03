package com.roni.gymcraft.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.roni.gymcraft.designsystem.sprites.drawIdleSprite
import com.roni.gymcraft.designsystem.sprites.drawPlaceholder
import com.roni.gymcraft.designsystem.sprites.rememberIdleClock
import com.roni.gymcraft.designsystem.sprites.rememberSprite

/**
 * The base picture (game-design.md §2, §10): the HQ on the left, the production buildings beside it, the standing
 * army in front with one count badge above each squad and its wounded below (game-design.md §6). Tap a building to open its sheet. Idle animation at 30 fps,
 * read only while drawing; labels are measured once per change, never per frame.
 */
@Composable
fun BaseView(game: GameUi, onAction: (HomeAction) -> Unit, modifier: Modifier = Modifier) {
    val hq = rememberSprite(game.hq)
    val buildings = game.buildings.map { b -> key(b.art) { rememberSprite(b.art) } }
    val army = game.army.map { q -> key(q.art) { rememberSprite(q.art) } }
    val colors = MaterialTheme.colorScheme
    val measurer = rememberTextMeasurer()
    val label = MaterialTheme.typography.labelMedium.copy(color = colors.onSurface, fontWeight = FontWeight.Bold)
    val muted = MaterialTheme.typography.labelMedium.copy(color = colors.onSurfaceVariant)
    val occupied = MaterialTheme.typography.labelLarge.copy(color = colors.error, fontWeight = FontWeight.Bold)
    val buildingLabels = remember(game.buildings, label) { game.buildings.map { measurer.measure("${it.name} · Lv ${it.level}", label) } }
    val occupiedLabel = remember(occupied) { measurer.measure("OCCUPIED", occupied) }
    val badges = remember(game.army, label) { game.army.map { q -> measurer.measure("×${q.ready}", label) } }
    val hurt = remember(game.army, muted) { game.army.map { q -> if (q.wounded > 0) measurer.measure("+${q.wounded} hurt", muted) else null } }
    val emptyLabel = remember(muted) { measurer.measure("No troops yet. Tap a building to train.", muted) }
    val hits = remember(game.buildings.size) { FloatArray(game.buildings.size * 4) }
    val ids by rememberUpdatedState(game.buildings.map { it.id })
    val action by rememberUpdatedState(onAction)
    val clock = rememberIdleClock()
    val sky = colors.surface
    val ground = Color(0xFF3A3226)

    Canvas(
        modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
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
        drawRect(sky)
        drawRect(ground, topLeft = Offset(0f, h * 0.58f))

        // HQ
        val hqH = h * 0.5f
        val hqW = hq?.let { hqH * it.art.width / it.art.height } ?: hqH
        val hqX = w * 0.03f + hqW / 2f
        if (hq != null) drawIdleSprite(hq, hqX, h * 0.64f, hqH, t) else drawPlaceholder(hqX, h * 0.64f, hqH)

        // Production buildings
        val x0 = hqX + hqW / 2f + w * 0.02f
        val slot = if (buildings.isEmpty()) 0f else (w * 0.98f - x0) / buildings.size
        for (i in buildings.indices) {
            val b = game.buildings.getOrNull(i) ?: continue
            val sprite = buildings[i]
            val ratio = sprite?.let { it.art.height / it.art.width } ?: 0.8f
            val bh = minOf(h * 0.4f, slot * 0.95f * ratio)
            val bw = bh / ratio
            val cx = x0 + slot * (i + 0.5f)
            val bottom = h * 0.6f
            val a = if (b.occupied) 0.45f else 1f
            if (sprite != null) drawIdleSprite(sprite, cx, bottom, bh, t, phase = i * 0.7f, alpha = a) else drawPlaceholder(cx, bottom, bh, a)
            val text = buildingLabels.getOrNull(i)
            if (text != null) drawText(text, topLeft = Offset(cx - text.size.width / 2f, bottom + 2f))
            if (b.occupied) {
                drawText(occupiedLabel, topLeft = Offset(cx - occupiedLabel.size.width / 2f, bottom - bh / 2f - occupiedLabel.size.height / 2f))
            }
            hits[i * 4] = cx - bw / 2f
            hits[i * 4 + 1] = bottom - bh
            hits[i * 4 + 2] = cx + bw / 2f
            hits[i * 4 + 3] = bottom + (text?.size?.height ?: 0) + 4f
        }

        // The army, one sprite per unit type with its count (game-design.md §6)
        if (army.isEmpty()) {
            drawText(emptyLabel, topLeft = Offset((w - emptyLabel.size.width) / 2f, h * 0.8f))
        } else {
            val uh = h * 0.24f
            val unitSlot = w * 0.94f / maxOf(army.size, 4)
            val bottom = h * 0.9f
            for (i in army.indices) {
                val q = game.army.getOrNull(i) ?: continue
                val sprite = army[i]
                val cx = w * 0.03f + unitSlot * (i + 0.5f)
                val a = if (q.ready == 0) 0.4f else 1f
                if (sprite != null) drawIdleSprite(sprite, cx, bottom, uh, t, phase = i * 1.3f, alpha = a) else drawPlaceholder(cx, bottom, uh, a)
                val badge = badges.getOrNull(i) ?: continue
                drawText(badge, topLeft = Offset(cx - badge.size.width / 2f, bottom - uh - badge.size.height))
                val wounded = hurt.getOrNull(i) ?: continue
                drawText(wounded, topLeft = Offset(cx - wounded.size.width / 2f, bottom + 1f))
            }
        }
    }
}
