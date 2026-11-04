package com.roni.gymcraft.designsystem

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * "I went" as a lift (game-design.md §12): hold it and a barbell rises; let go early and it drops back. Locking it
 * out at the top buzzes once and calls [onConfirmed]. The bar's height is read only while drawing.
 */
@Composable
fun LiftButton(
    text: String,
    enabled: Boolean,
    onConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    holdMillis: Int = 1000,
) {
    val colors = MaterialTheme.colorScheme
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val confirm by rememberUpdatedState(onConfirmed)
    val primary = colors.primary
    Box(
        modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (enabled) primary.copy(alpha = 0.22f) else colors.surface)
            .drawBehind {
                val p = progress.value
                drawRect(primary.copy(alpha = 0.45f), topLeft = Offset(0f, size.height * (1f - p)), size = Size(size.width, size.height * p))
                drawBarbell(p, if (enabled) 1f else 0.35f)
            }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                awaitEachGesture {
                    awaitFirstDown()
                    val lift = scope.launch {
                        progress.animateTo(1f, tween(holdMillis, easing = FastOutSlowInEasing))
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        confirm()
                        progress.animateTo(0f, tween(400))
                    }
                    waitForUpOrCancellation()
                    if (lift.isActive && progress.targetValue == 1f) {
                        lift.cancel()
                        scope.launch { progress.animateTo(0f, tween(250)) }
                    }
                }
            }
            .testTag("lift_button"),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (enabled) colors.onSurface else colors.onSurfaceVariant,
        )
    }
}

private val Steel = Color(0xFFC9CED2)
private val Plate = Color(0xFF3B3F44)
private val Outline = Color(0xFF15110D)

/** A barbell across the button: on the floor at [p] = 0, locked out at the top at 1. */
private fun DrawScope.drawBarbell(p: Float, alpha: Float) {
    val u = size.height / 88f
    val barW = size.width * 0.62f
    val left = (size.width - barW) / 2f
    val y = 36f * u - 22f * u * p
    val outline = Stroke(width = 2.5f * u)
    drawRoundRect(Steel, Offset(left, y - 2.5f * u), Size(barW, 5f * u), CornerRadius(2.5f * u), alpha = alpha)
    drawRoundRect(Outline, Offset(left, y - 2.5f * u), Size(barW, 5f * u), CornerRadius(2.5f * u), style = outline, alpha = alpha)
    for (side in 0..1) {
        val big = if (side == 0) left + 6f * u else left + barW - 18f * u
        val small = if (side == 0) left + 20f * u else left + barW - 28f * u
        drawRoundRect(Plate, Offset(big, y - 13f * u), Size(12f * u, 26f * u), CornerRadius(3f * u), alpha = alpha)
        drawRoundRect(Outline, Offset(big, y - 13f * u), Size(12f * u, 26f * u), CornerRadius(3f * u), style = outline, alpha = alpha)
        drawRoundRect(Plate, Offset(small, y - 9f * u), Size(8f * u, 18f * u), CornerRadius(2f * u), alpha = alpha)
        drawRoundRect(Outline, Offset(small, y - 9f * u), Size(8f * u, 18f * u), CornerRadius(2f * u), style = outline, alpha = alpha)
    }
}
