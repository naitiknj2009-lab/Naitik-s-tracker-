package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.AmberRevise
import com.example.ui.theme.AmberReviseDim
import com.example.ui.theme.CrimsonPending
import com.example.ui.theme.CrimsonPendingDim
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.EmeraldDone
import com.example.ui.theme.EmeraldDoneDim
import com.example.ui.theme.GlassBorderBottom
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassBorderTop
import com.example.ui.theme.GlassCard
import com.example.ui.theme.GlassSurfaceDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.glassSurface

@Composable
fun GlassCardBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = GlassCard,
    borderTop: Color = GlassBorderTop,
    borderBottom: Color = GlassBorderBottom,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .glassSurface(
                shapeRadius = cornerRadius,
                backgroundColor = backgroundColor,
                borderTopColor = borderTop,
                borderBottomColor = borderBottom
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun MilestoneButton(
    type: CheckpointType,
    label: String,
    status: CheckpointStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = when (status) {
            CheckpointStatus.DONE -> EmeraldDoneDim
            CheckpointStatus.REVISE -> AmberReviseDim
            CheckpointStatus.PENDING -> CrimsonPendingDim
        },
        label = "bg_color"
    )

    val borderColor by animateColorAsState(
        targetValue = when (status) {
            CheckpointStatus.DONE -> EmeraldDone.copy(alpha = 0.8f)
            CheckpointStatus.REVISE -> AmberRevise.copy(alpha = 0.8f)
            CheckpointStatus.PENDING -> GlassBorderSubtle
        },
        label = "border_color"
    )

    val contentColor by animateColorAsState(
        targetValue = when (status) {
            CheckpointStatus.DONE -> EmeraldDone
            CheckpointStatus.REVISE -> AmberRevise
            CheckpointStatus.PENDING -> TextSecondary
        },
        label = "content_color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .testTag("checkpoint_${type.name.lowercase()}")
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = contentColor),
                onClick = onClick
            )
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (status) {
                CheckpointStatus.DONE -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = EmeraldDone,
                        modifier = Modifier.size(20.dp)
                    )
                }
                CheckpointStatus.REVISE -> {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Revise Again",
                        tint = AmberRevise,
                        modifier = Modifier.size(18.dp)
                    )
                }
                CheckpointStatus.PENDING -> {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(CrimsonPending.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LegendBar(modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DeepSpaceSlate.copy(alpha = 0.6f))
            .border(1.dp, GlassBorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(CrimsonPending.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("■ Pending", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Normal)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = EmeraldDone,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("✓ Done", fontSize = 11.sp, color = EmeraldDone, fontWeight = FontWeight.SemiBold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = AmberRevise,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("→ Revise again", fontSize = 11.sp, color = AmberRevise, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun GlassFilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    count: Int? = null,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) NeonCyan.copy(alpha = 0.2f) else GlassSurfaceDark
    val border = if (isSelected) NeonCyan else GlassBorderSubtle
    val textClr = if (isSelected) NeonCyan else TextSecondary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = bg,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textClr
            )
            if (count != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) NeonCyan else TextMuted.copy(alpha = 0.3f))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "$count",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) DeepSpaceSlate else TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressDonut(
    fraction: Float,
    size: Dp = 80.dp,
    strokeWidth: Dp = 8.dp,
    primaryColor: Color = NeonCyan,
    trackColor: Color = GlassBorderSubtle,
    modifier: Modifier = Modifier,
    centerContent: @Composable () -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "progress_anim"
    )

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(size),
            color = trackColor,
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round
        )
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(size),
            color = primaryColor,
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round
        )
        centerContent()
    }
}
