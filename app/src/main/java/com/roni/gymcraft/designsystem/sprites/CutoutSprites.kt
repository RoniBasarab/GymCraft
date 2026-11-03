package com.roni.gymcraft.designsystem.sprites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.roni.gymcraft.designsystem.GymColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/** The cutout parts of one squad, built once (architecture.md §11). Sizes are pixels at the current density. */
class CutoutSprites(
    val body: ImageBitmap,
    val weapon: ImageBitmap,
    val flash: ImageBitmap,
    val stripe: ImageBitmap,
    /** The width/height of every part bitmap, in pixels. */
    val partSize: Int,
    /** "×1" … "×9" measured once; the battlefield only draws them. */
    val badges: List<TextLayoutResult>,
)

@Composable
fun rememberCutoutSprites(): CutoutSprites {
    val density = LocalDensity.current.density
    val measurer = rememberTextMeasurer()
    val badges = remember(measurer) {
        (1..9).map { n ->
            measurer.measure(
                text = AnnotatedString("×$n"),
                style = TextStyle(
                    color = GymColors.Muted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
    return remember(density, badges) { buildCutoutSprites(density, badges) }
}

private const val PART_DP = 64f

private val Outline = Color(0xFF241E18)
private val BodyFill = Color(0xFF7A6A52)
private val HelmetFill = Color(0xFF4E5A3C)
private val VisorFill = Color(0xFF9FD3E8)
private val WeaponFill = Color(0xFF8A8A8A)
private val FlashFill = Color(0xFFF2C744)
private val StripeFill = Color(0xFFE07B28)

private fun buildCutoutSprites(density: Float, badges: List<TextLayoutResult>): CutoutSprites {
    val partSize = (PART_DP * density).roundToInt().coerceAtLeast(1)
    return CutoutSprites(
        body = part(partSize) { drawBody() },
        weapon = part(partSize) { drawWeapon() },
        flash = part(partSize) { drawFlash() },
        stripe = part(partSize) { drawStripe() },
        partSize = partSize,
        badges = badges,
    )
}

/** Draws one part into a fresh [ImageBitmap]; coordinates are fractions of the canvas. */
private fun part(sizePx: Int, draw: DrawScope.() -> Unit): ImageBitmap {
    val image = ImageBitmap(sizePx, sizePx)
    val canvas = Canvas(image)
    CanvasDrawScope().draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = Size(sizePx.toFloat(), sizePx.toFloat()),
        block = draw,
    )
    return image
}

private fun DrawScope.drawBody() {
    val w = size.width
    val h = size.height
    val outline = Stroke(width = w * 0.045f)

    // Rounded body.
    drawRoundRect(
        color = BodyFill,
        topLeft = Offset(w * 0.20f, h * 0.30f),
        size = Size(w * 0.60f, h * 0.55f),
        cornerRadius = CornerRadius(w * 0.18f, h * 0.18f),
    )
    drawRoundRect(
        color = Outline,
        topLeft = Offset(w * 0.20f, h * 0.30f),
        size = Size(w * 0.60f, h * 0.55f),
        cornerRadius = CornerRadius(w * 0.18f, h * 0.18f),
        style = outline,
    )

    // Round helmet.
    drawCircle(color = HelmetFill, radius = w * 0.22f, center = Offset(w * 0.50f, h * 0.24f))
    drawCircle(color = Outline, radius = w * 0.22f, center = Offset(w * 0.50f, h * 0.24f), style = outline)

    // Light-blue visor.
    drawRoundRect(
        color = VisorFill,
        topLeft = Offset(w * 0.34f, h * 0.17f),
        size = Size(w * 0.32f, h * 0.12f),
        cornerRadius = CornerRadius(w * 0.06f, h * 0.06f),
    )
}

private fun DrawScope.drawWeapon() {
    val w = size.width
    val h = size.height
    drawRoundRect(
        color = WeaponFill,
        topLeft = Offset(w * 0.30f, h * 0.44f),
        size = Size(w * 0.60f, h * 0.10f),
        cornerRadius = CornerRadius(h * 0.05f, h * 0.05f),
    )
    drawRoundRect(
        color = Outline,
        topLeft = Offset(w * 0.30f, h * 0.44f),
        size = Size(w * 0.60f, h * 0.10f),
        cornerRadius = CornerRadius(h * 0.05f, h * 0.05f),
        style = Stroke(width = w * 0.035f),
    )
}

private fun DrawScope.drawFlash() {
    val w = size.width
    val h = size.height
    drawPath(starPath(w * 0.55f, h * 0.40f, w * 0.26f, h * 0.11f), color = FlashFill)
}

private fun starPath(cx: Float, cy: Float, outer: Float, inner: Float): Path {
    val path = Path()
    for (k in 0 until 10) {
        val radius = if (k % 2 == 0) outer else inner
        val angle = (-90 + k * 36) * PI / 180.0
        val px = cx + (radius * cos(angle)).toFloat()
        val py = cy + (radius * sin(angle)).toFloat()
        if (k == 0) path.moveTo(px, py) else path.lineTo(px, py)
    }
    path.close()
    return path
}

private fun DrawScope.drawStripe() {
    val w = size.width
    val h = size.height
    drawRoundRect(
        color = StripeFill,
        topLeft = Offset(w * 0.22f, h * 0.36f),
        size = Size(w * 0.10f, h * 0.44f),
        cornerRadius = CornerRadius(w * 0.04f, h * 0.04f),
    )
}
