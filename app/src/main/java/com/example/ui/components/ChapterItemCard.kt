package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.AmberRevise
import com.example.ui.theme.CoralAlert
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldDone
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ChapterItemCard(
    chapter: ChapterEntity,
    accentColor: Color,
    onCycleCheckpoint: (CheckpointType) -> Unit,
    onUpdateRemarks: (String) -> Unit,
    onToggleWeak: () -> Unit,
    onToggleStrong: () -> Unit,
    onMarkAllDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRemarksDialog by remember { mutableStateOf(false) }
    var currentRemarks by remember(chapter.statusRemarks) { mutableStateOf(chapter.statusRemarks) }

    GlassCardBox(
        modifier = modifier
            .testTag("chapter_card_${chapter.sNo}")
            .fillMaxWidth(),
        cornerRadius = 16.dp,
        backgroundColor = if (chapter.completedCount == 7) EmeraldDone.copy(alpha = 0.08f) else GlassCard
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: S.No, Chapter Name, and badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // S.No Badge
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(1.dp, accentColor.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", chapter.sNo),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${chapter.completedCount}/7 milestones",
                            fontSize = 11.sp,
                            color = if (chapter.completedCount == 7) EmeraldDone else TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        if (chapter.hasRevision) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AmberRevise.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "Needs Revision",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberRevise
                                )
                            }
                        }
                    }
                }

                // Quick buttons: Weak area toggle, Strong star toggle
                IconButton(
                    onClick = onToggleWeak,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (chapter.isWeakTopic) Icons.Default.Warning else Icons.Outlined.WarningAmber,
                        contentDescription = "Mark Weak Topic",
                        tint = if (chapter.isWeakTopic) CoralAlert else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onToggleStrong,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (chapter.isStrongTopic) Icons.Default.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Mark Strong Topic",
                        tint = if (chapter.isStrongTopic) AmberRevise else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thin progress bar
            LinearProgressIndicator(
                progress = { chapter.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (chapter.completedCount == 7) EmeraldDone else accentColor,
                trackColor = GlassBorderSubtle,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            // The 7 Milestone Checkpoints
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MilestoneButton(
                    type = CheckpointType.LECTURE,
                    label = "Lecture",
                    status = chapter.lecture,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.LECTURE) }
                )
                MilestoneButton(
                    type = CheckpointType.DPP,
                    label = "DPP",
                    status = chapter.dpp,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.DPP) }
                )
                MilestoneButton(
                    type = CheckpointType.PYQ,
                    label = "PYQ's",
                    status = chapter.pyq,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.PYQ) }
                )
                MilestoneButton(
                    type = CheckpointType.REPLICA,
                    label = "Replica",
                    status = chapter.replicaSheet,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.REPLICA) }
                )
                MilestoneButton(
                    type = CheckpointType.NOTES,
                    label = "Notes",
                    status = chapter.summaryNotes,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.NOTES) }
                )
                MilestoneButton(
                    type = CheckpointType.TEST,
                    label = "Test",
                    status = chapter.test,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.TEST) }
                )
                MilestoneButton(
                    type = CheckpointType.ANALYSIS,
                    label = "Analysis",
                    status = chapter.testAnalysis,
                    modifier = Modifier.weight(1f),
                    onClick = { onCycleCheckpoint(CheckpointType.ANALYSIS) }
                )
            }

            // Status / Remarks Row
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepSpaceSlate.copy(alpha = 0.5f))
                    .border(1.dp, GlassBorderSubtle, RoundedCornerShape(8.dp))
                    .clickable { showRemarksDialog = true }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Remarks: ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        text = if (chapter.statusRemarks.isNotBlank()) chapter.statusRemarks else "Tap to add study notes / status…",
                        fontSize = 11.sp,
                        color = if (chapter.statusRemarks.isNotBlank()) TextPrimary else TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit remarks",
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }

    if (showRemarksDialog) {
        AlertDialog(
            onDismissRequest = { showRemarksDialog = false },
            containerColor = DeepSpaceSlate,
            title = {
                Text(
                    text = "Chapter Notes & Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "${chapter.sNo}. ${chapter.name}",
                        fontSize = 13.sp,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = currentRemarks,
                        onValueChange = { currentRemarks = it },
                        label = { Text("Status / Remarks / Weak points") },
                        placeholder = { Text("e.g. Revisit Doppler effect PYQs 2023...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("remarks_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = GlassBorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        minLines = 3,
                        maxLines = 6
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onMarkAllDone()
                                showRemarksDialog = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDone)
                        ) {
                            Text("All Done", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateRemarks(currentRemarks)
                        showRemarksDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Save", color = DeepSpaceSlate, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemarksDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
