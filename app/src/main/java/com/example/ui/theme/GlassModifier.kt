package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassSurface(
    shapeRadius: Dp = 16.dp,
    backgroundColor: Color = GlassCard,
    borderTopColor: Color = GlassBorderTop,
    borderBottomColor: Color = GlassBorderBottom,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(RoundedCornerShape(shapeRadius))
    .background(
        Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = backgroundColor.alpha.coerceAtLeast(0.35f)),
                backgroundColor.copy(alpha = (backgroundColor.alpha * 0.75f).coerceAtLeast(0.2f))
            )
        )
    )
    .border(
        width = borderWidth,
        brush = Brush.verticalGradient(
            colors = listOf(borderTopColor, borderBottomColor)
        ),
        shape = RoundedCornerShape(shapeRadius)
    )

fun Modifier.ambientCosmicGlow(): Modifier = this.drawBehind {
    // Top-left Electric Violet ambient orb
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x358B5CF6),
                Color(0x108B5CF6),
                Color.Transparent
            ),
            center = Offset(size.width * 0.15f, size.height * 0.12f),
            radius = size.width * 0.65f
        )
    )
    // Center-right Neon Cyan ambient orb
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x2806B6D4),
                Color(0x0C06B6D4),
                Color.Transparent
            ),
            center = Offset(size.width * 0.85f, size.height * 0.45f),
            radius = size.width * 0.6f
        )
    )
    // Bottom-left Emerald glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x2010B981),
                Color.Transparent
            ),
            center = Offset(size.width * 0.2f, size.height * 0.85f),
            radius = size.width * 0.5f
        )
    )
}
