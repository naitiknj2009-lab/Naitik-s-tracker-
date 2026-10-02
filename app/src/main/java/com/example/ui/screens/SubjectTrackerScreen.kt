package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CheckpointStatus
import com.example.data.ChapterEntity
import com.example.data.SubjectType
import com.example.ui.components.ChapterItemCard
import com.example.ui.components.GlassCardBox
import com.example.ui.components.GlassFilterPill
import com.example.ui.components.LegendBar
import com.example.ui.components.ProgressDonut
import com.example.ui.theme.ChemistryViolet
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.EmeraldDone
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.MathEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PhysicsCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.ChapterFilter
import com.example.ui.viewmodel.JeeViewModel
import com.example.ui.viewmodel.SubjectStats

@Composable
fun SubjectTrackerScreen(
    subject: SubjectType,
    chapters: List<ChapterEntity>,
    stats: SubjectStats,
    searchQuery: String,
    currentFilter: ChapterFilter,
    viewModel: JeeViewModel,
    modifier: Modifier = Modifier
) {
    val accentColor = when (subject) {
        SubjectType.PHYSICS -> PhysicsCyan
        SubjectType.CHEMISTRY -> ChemistryViolet
        SubjectType.MATHEMATICS -> MathEmerald
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Subject Header Banner
        GlassCardBox(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = accentColor.copy(alpha = 0.08f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${subject.displayName} Tracker",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${stats.completedChapters}/${stats.totalChapters} chapters mastered • ${stats.completedCheckpoints}/${stats.totalCheckpoints} checkpoints",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    if (stats.weakTopicsCount > 0) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "⚠ ${stats.weakTopicsCount} weak focus areas marked",
                            fontSize = 11.sp,
                            color = Color(0xFFF43F5E),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                ProgressDonut(
                    fraction = stats.progressFraction,
                    size = 64.dp,
                    strokeWidth = 6.dp,
                    primaryColor = accentColor
                ) {
                    Text(
                        text = "${(stats.progressFraction * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search ${subject.displayName} chapters...", color = TextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("chapter_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DeepSpaceSlate.copy(alpha = 0.6f),
                unfocusedContainerColor = DeepSpaceSlate.copy(alpha = 0.4f),
                focusedBorderColor = accentColor,
                unfocusedBorderColor = GlassBorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Pills
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GlassFilterPill(
                    label = "All",
                    isSelected = currentFilter == ChapterFilter.ALL,
                    onClick = { viewModel.setChapterFilter(ChapterFilter.ALL) },
                    count = stats.totalChapters
                )
            }
            item {
                GlassFilterPill(
                    label = "In Progress",
                    isSelected = currentFilter == ChapterFilter.IN_PROGRESS,
                    onClick = { viewModel.setChapterFilter(ChapterFilter.IN_PROGRESS) }
                )
            }
            item {
                GlassFilterPill(
                    label = "Completed",
                    isSelected = currentFilter == ChapterFilter.COMPLETED,
                    onClick = { viewModel.setChapterFilter(ChapterFilter.COMPLETED) },
                    count = stats.completedChapters
                )
            }
            item {
                GlassFilterPill(
                    label = "Revise Again",
                    isSelected = currentFilter == ChapterFilter.NEEDS_REVISION,
                    onClick = { viewModel.setChapterFilter(ChapterFilter.NEEDS_REVISION) },
                    count = stats.revisionCount
                )
            }
            item {
                GlassFilterPill(
                    label = "Not Started",
                    isSelected = currentFilter == ChapterFilter.PENDING,
                    onClick = { viewModel.setChapterFilter(ChapterFilter.PENDING) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend Bar
        LegendBar(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        // Chapters List
        if (chapters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No chapters found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try adjusting your search or active filter.",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = chapters,
                    key = { it.id }
                ) { chapter ->
                    ChapterItemCard(
                        chapter = chapter,
                        accentColor = accentColor,
                        onCycleCheckpoint = { type ->
                            viewModel.cycleCheckpoint(chapter.id, type)
                        },
                        onUpdateRemarks = { remarks ->
                            viewModel.updateChapterRemarks(chapter.id, remarks)
                        },
                        onToggleWeak = {
                            viewModel.toggleWeakTopic(chapter)
                        },
                        onToggleStrong = {
                            viewModel.toggleStrongTopic(chapter)
                        },
                        onMarkAllDone = {
                            viewModel.markAllCheckpoints(chapter.id, CheckpointStatus.DONE)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
