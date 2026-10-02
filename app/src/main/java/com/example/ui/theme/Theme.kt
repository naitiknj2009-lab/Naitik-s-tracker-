package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = ObsidianCanvas,
    primaryContainer = NeonCyanDim,
    onPrimaryContainer = Color.White,
    secondary = ElectricViolet,
    onSecondary = ObsidianCanvas,
    secondaryContainer = ElectricVioletDim,
    onSecondaryContainer = Color.White,
    tertiary = EmeraldDone,
    onTertiary = ObsidianCanvas,
    background = ObsidianCanvas,
    onBackground = TextPrimary,
    surface = DeepSpaceSlate,
    onSurface = TextPrimary,
    surfaceVariant = MidnightSurface,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorderSubtle
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
