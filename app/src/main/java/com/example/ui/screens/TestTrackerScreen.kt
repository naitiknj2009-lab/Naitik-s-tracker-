package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TestEntity
import com.example.ui.components.GlassCardBox
import com.example.ui.components.GlassFilterPill
import com.example.ui.theme.AmberRevise
import com.example.ui.theme.CoralAlert
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldDone
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TestAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.JeeViewModel
import com.example.ui.viewmodel.TestTabFilter

@Composable
fun TestTrackerScreen(
    tests: List<TestEntity>,
    currentFilter: TestTabFilter,
    viewModel: JeeViewModel,
    modifier: Modifier = Modifier
) {
    var editingTest by remember { mutableStateOf<TestEntity?>(null) }

    val totalCount = tests.size
    val attemptedCount = tests.count { it.attempted }
    val bestScore = tests.filter { it.attempted }.maxOfOrNull { it.score } ?: 0
    val avgScore = if (attemptedCount > 0) {
        tests.filter { it.attempted }.map { it.score }.average().toInt()
    } else 0
    val analysisDoneCount = tests.count { it.analysisDone }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Test Stats Header Card
        GlassCardBox(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = TestAmber.copy(alpha = 0.08f)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Test Series & Analysis",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Mission 100 JEE 2027 • 32 Target Tests",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TestAmber.copy(alpha = 0.2f))
                            .border(1.dp, TestAmber, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$attemptedCount / 32 Attempted",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TestAmber
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Best Score", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (attemptedCount > 0) "$bestScore/300" else "—",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDone
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Avg Score", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (attemptedCount > 0) "$avgScore/300" else "—",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Analysis Done", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$analysisDoneCount / 32",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricViolet
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Strategy Callout Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DeepSpaceSlate.copy(alpha = 0.5f))
                .border(1.dp, GlassBorderSubtle, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "💡 Strategy: Record score → Identify weak chapters → Reattempt wrong questions → Schedule revision.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Pills for Tests
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TestTabFilter.values().forEach { filter ->
                item {
                    GlassFilterPill(
                        label = filter.label,
                        isSelected = currentFilter == filter,
                        onClick = { viewModel.setTestFilter(filter) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tests List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = tests,
                key = { it.id }
            ) { test ->
                TestCardItem(
                    test = test,
                    onEditClick = { editingTest = test },
                    onToggleAttempted = {
                        viewModel.updateTest(test.copy(attempted = !test.attempted))
                    },
                    onToggleAnalysis = {
                        viewModel.updateTest(test.copy(analysisDone = !test.analysisDone))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Edit Test Dialog
    editingTest?.let { test ->
        EditTestDialog(
            test = test,
            onDismiss = { editingTest = null },
            onSave = { updated ->
                viewModel.updateTest(updated)
                editingTest = null
            }
        )
    }
}

@Composable
fun TestCardItem(
    test: TestEntity,
    onEditClick: () -> Unit,
    onToggleAttempted: () -> Unit,
    onToggleAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAdv = test.pattern.contains("Advanced", ignoreCase = true)
    val patternColor = if (isAdv) CoralAlert else NeonCyan

    GlassCardBox(
        modifier = modifier
            .testTag("test_card_${test.sNo}")
            .fillMaxWidth(),
        cornerRadius = 14.dp,
        backgroundColor = if (test.attempted) EmeraldDone.copy(alpha = 0.05f) else DeepSpaceSlate.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(TestAmber.copy(alpha = 0.2f))
                            .border(1.dp, TestAmber.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${test.sNo}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TestAmber
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = test.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${test.type} • ${test.date}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(patternColor.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = test.pattern,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = patternColor
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit test",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Score and Status row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepSpaceSlate.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Score pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Score: ",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = if (test.attempted) "${test.score}/300" else "Unattempted",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (test.attempted) EmeraldDone else TextMuted
                    )
                    if (test.attempted && test.accuracy > 0f) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${test.accuracy.toInt()}% Acc)",
                            fontSize = 11.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Checkboxes for Attempted & Analysis Done
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onToggleAttempted() }
                    ) {
                        Checkbox(
                            checked = test.attempted,
                            onCheckedChange = { onToggleAttempted() },
                            colors = CheckboxDefaults.colors(
                                checkedColor = EmeraldDone,
                                uncheckedColor = GlassBorderSubtle
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Attempted", fontSize = 11.sp, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onToggleAnalysis() }
                    ) {
                        Checkbox(
                            checked = test.analysisDone,
                            onCheckedChange = { onToggleAnalysis() },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ElectricViolet,
                                uncheckedColor = GlassBorderSubtle
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Analysis", fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }

            // Mistakes / topics note if any
            if (test.mistakesTopics.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "📝 Mistakes/Focus: ${test.mistakesTopics}",
                    fontSize = 11.sp,
                    color = AmberRevise,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EditTestDialog(
    test: TestEntity,
    onDismiss: () -> Unit,
    onSave: (TestEntity) -> Unit
) {
    var attempted by remember { mutableStateOf(test.attempted) }
    var scoreText by remember { mutableStateOf(if (test.score > 0) test.score.toString() else "") }
    var accuracyText by remember { mutableStateOf(if (test.accuracy > 0f) test.accuracy.toInt().toString() else "") }
    var mistakesText by remember { mutableStateOf(test.mistakesTopics) }
    var analysisDone by remember { mutableStateOf(test.analysisDone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepSpaceSlate,
        title = {
            Text(
                text = "${test.name} Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${test.type} • ${test.pattern} (${test.date})",
                    fontSize = 12.sp,
                    color = TestAmber
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mark Attempted", fontSize = 13.sp, color = TextPrimary)
                    Switch(
                        checked = attempted,
                        onCheckedChange = { attempted = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = EmeraldDone,
                            checkedTrackColor = EmeraldDone.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = scoreText,
                        onValueChange = { scoreText = it },
                        label = { Text("Score (/300)") },
                        placeholder = { Text("e.g. 185") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("test_score_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = GlassBorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                        value = accuracyText,
                        onValueChange = { accuracyText = it },
                        label = { Text("Accuracy %") },
                        placeholder = { Text("e.g. 82") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = GlassBorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = mistakesText,
                    onValueChange = { mistakesText = it },
                    label = { Text("Mistakes / Weak Chapters") },
                    placeholder = { Text("e.g. Rotational dynamics negative marks, revise Organic reagents...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("test_mistakes_input"),
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricViolet,
                        unfocusedBorderColor = GlassBorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Test Analysis Done", fontSize = 13.sp, color = TextPrimary)
                    Checkbox(
                        checked = analysisDone,
                        onCheckedChange = { analysisDone = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ElectricViolet,
                            uncheckedColor = GlassBorderSubtle
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedScore = scoreText.toIntOrNull() ?: 0
                    val parsedAcc = accuracyText.toFloatOrNull() ?: 0f
                    val updated = test.copy(
                        attempted = attempted || parsedScore > 0,
                        score = parsedScore,
                        accuracy = parsedAcc,
                        mistakesTopics = mistakesText,
                        analysisDone = analysisDone
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = TestAmber)
            ) {
                Text("Save", color = DeepSpaceSlate, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
