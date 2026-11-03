package com.roni.gymcraft.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** The canvas palette (architecture.md §11); Canvas drawing uses these directly. */
object GymColors {
    val Background = Color(0xFF1C1915)
    val Surface = Color(0xFF28231D)
    val OnBackground = Color(0xFFF1E9DA)
    val OnSurface = Color(0xFFF1E9DA)
    val Primary = Color(0xFFE07B28)
    val OnPrimary = Color(0xFF1C1915)
    val Muted = Color(0xFFBCAE96)
}

private val DarkColors = darkColorScheme(
    background = GymColors.Background,
    surface = GymColors.Surface,
    onBackground = GymColors.OnBackground,
    onSurface = GymColors.OnSurface,
    primary = GymColors.Primary,
    onPrimary = GymColors.OnPrimary,
    onSurfaceVariant = GymColors.Muted,
)

@Composable
fun GymCraftTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}
