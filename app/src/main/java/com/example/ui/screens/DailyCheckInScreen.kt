package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DailyCheckInEntity
import com.example.data.WeeklyReviewEntity
import com.example.ui.components.GlassCardBox
import com.example.ui.theme.AmberRevise
import com.example.ui.theme.CoralAlert
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldDone
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.JeeViewModel

@Composable
fun DailyCheckInScreen(
    todayCheckIn: DailyCheckInEntity?,
    weeklyReviews: List<WeeklyReviewEntity>,
    viewModel: JeeViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Today's Check-in, 1: Weekly Progress

    // Daily state
    var whatCompleted by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.whatCompleted ?: "") }
    var whatNotCompleted by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.whatNotCompleted ?: "") }
    var whyDistraction by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.whyDistraction ?: "") }
    var task1 by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.task1 ?: "") }
    var task2 by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.task2 ?: "") }
    var task3 by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.task3 ?: "") }
    var backlog by remember(todayCheckIn) { mutableStateOf(todayCheckIn?.backlog ?: "") }
    var confidence by remember(todayCheckIn) { mutableIntStateOf(todayCheckIn?.confidence ?: 8) }
    var energy by remember(todayCheckIn) { mutableIntStateOf(todayCheckIn?.energy ?: 8) }
    var sleepHours by remember(todayCheckIn) { mutableFloatStateOf(todayCheckIn?.sleepHours ?: 7.0f) }

    var editingWeek by remember { mutableStateOf<WeeklyReviewEntity?>(null) }
    var showSavedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Golden Rule Banner
        GlassCardBox(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = ElectricViolet.copy(alpha = 0.08f)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = ElectricViolet,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "“Track the process, not just marks. If a box is unfinished, move it forward instead of hiding it.”",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "— Mission 100 JEE 2027 Core Principle",
                        fontSize = 10.sp,
                        color = ElectricViolet,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sub-tabs: Today's Check-in / Weekly Progress
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = DeepSpaceSlate.copy(alpha = 0.6f),
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedSubTab])
                        .height(3.dp)
                        .background(NeonCyan, RoundedCornerShape(2.dp))
                )
            },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, GlassBorderSubtle, RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        text = "Today's Check-in",
                        fontSize = 13.sp,
                        fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        text = "Weekly Tracking (8 Weeks)",
                        fontSize = 13.sp,
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedSubTab == 0) {
            // Today's Check-in
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily Self-Check (${viewModel.todayDateString})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Button(
                            onClick = {
                                viewModel.saveTodayCheckIn(
                                    whatCompleted = whatCompleted,
                                    whatNotCompleted = whatNotCompleted,
                                    whyDistraction = whyDistraction,
                                    task1 = task1,
                                    task2 = task2,
                                    task3 = task3,
                                    backlog = backlog,
                                    confidence = confidence,
                                    energy = energy,
                                    sleepHours = sleepHours
                                )
                                showSavedMessage = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            modifier = Modifier.testTag("save_daily_checkin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                tint = DeepSpaceSlate,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Entry", color = DeepSpaceSlate, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                if (showSavedMessage) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldDone.copy(alpha = 0.2f))
                                .border(1.dp, EmeraldDone, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldDone, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Check-in saved to local tracker database!", color = EmeraldDone, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                // Wellness Sliders: Confidence, Energy, Sleep
                item {
                    GlassCardBox(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "Daily Vitals (1-10)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Confidence
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SentimentSatisfiedAlt, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Confidence: $confidence/10", fontSize = 12.sp, color = TextPrimary)
                                }
                            }
                            Slider(
                                value = confidence.toFloat(),
                                onValueChange = { confidence = it.toInt() },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                            )

                            // Energy
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = EmeraldDone, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Energy Level: $energy/10", fontSize = 12.sp, color = TextPrimary)
                                }
                            }
                            Slider(
                                value = energy.toFloat(),
                                onValueChange = { energy = it.toInt() },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(thumbColor = EmeraldDone, activeTrackColor = EmeraldDone)
                            )

                            // Sleep
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Bedtime, contentDescription = null, tint = ElectricViolet, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Sleep: ${String.format("%.1f", sleepHours)} hours", fontSize = 12.sp, color = TextPrimary)
                                }
                            }
                            Slider(
                                value = sleepHours,
                                onValueChange = { sleepHours = it },
                                valueRange = 3f..12f,
                                steps = 17,
                                colors = SliderDefaults.colors(thumbColor = ElectricViolet, activeTrackColor = ElectricViolet)
                            )
                        }
                    }
                }

                // Reflections: Completed / Not completed / Why
                item {
                    GlassCardBox(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "Today's Reflections (Write One Line)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = whatCompleted,
                                onValueChange = { whatCompleted = it },
                                label = { Text("What I completed") },
                                placeholder = { Text("e.g. Waves DPP 03 + Modern Physics Lecture 04") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldDone,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = whatNotCompleted,
                                onValueChange = { whatNotCompleted = it },
                                label = { Text("What I did not complete") },
                                placeholder = { Text("e.g. Complex numbers PYQ 2022-2024") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CoralAlert,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = whyDistraction,
                                onValueChange = { whyDistraction = it },
                                label = { Text("Why / Distraction") },
                                placeholder = { Text("e.g. Late dinner, scrolling social media, felt fatigued") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberRevise,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }
                }

                // Tomorrow's Top 3 Tasks & Backlog
                item {
                    GlassCardBox(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "Tomorrow's Top 3 Tasks",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = task1,
                                onValueChange = { task1 = it },
                                label = { Text("Task 1") },
                                placeholder = { Text("e.g. Finish Rotational Dynamics revision sheet") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = task2,
                                onValueChange = { task2 = it },
                                label = { Text("Task 2") },
                                placeholder = { Text("e.g. Attempt JEE Mains Part Test 04") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = task3,
                                onValueChange = { task3 = it },
                                label = { Text("Task 3") },
                                placeholder = { Text("e.g. Hydrocarbons reaction chart notes") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = backlog,
                                onValueChange = { backlog = it },
                                label = { Text("Backlog to Clear") },
                                placeholder = { Text("e.g. Fluid mechanics pending lectures 3-5...") },
                                minLines = 2,
                                maxLines = 4,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ElectricViolet,
                                    unfocusedBorderColor = GlassBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        } else {
            // Weekly Progress Tracker (Weeks 1 to 8)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Weekly Self-Check Overview",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "“Honest tracking is more useful than perfect-looking numbers.”",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                items(weeklyReviews) { review ->
                    WeeklyReviewCard(
                        review = review,
                        onEdit = { editingWeek = review }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Weekly Edit Dialog
    editingWeek?.let { review ->
        EditWeeklyReviewDialog(
            review = review,
            onDismiss = { editingWeek = null },
            onSave = { updated ->
                viewModel.updateWeeklyReview(updated)
                editingWeek = null
            }
        )
    }
}

@Composable
fun WeeklyReviewCard(
    review: WeeklyReviewEntity,
    onEdit: () -> Unit
) {
    GlassCardBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        cornerRadius = 14.dp
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
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(ElectricViolet.copy(alpha = 0.2f))
                            .border(1.dp, ElectricViolet, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "W${review.weekNumber}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricViolet
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Week ${review.weekNumber}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Consistency: ${review.consistency}/10",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit week", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics row: Hours, Lectures, DPP, PYQ, Tests, Avg Score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepSpaceSlate.copy(alpha = 0.5f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hours", fontSize = 10.sp, color = TextSecondary)
                    Text("${review.studyHours}h", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Lectures", fontSize = 10.sp, color = TextSecondary)
                    Text("${review.lecturesDone}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("DPP", fontSize = 10.sp, color = TextSecondary)
                    Text("${review.dppDone}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("PYQ", fontSize = 10.sp, color = TextSecondary)
                    Text("${review.pyqDone}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tests", fontSize = 10.sp, color = TextSecondary)
                    Text("${review.testsDone}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Avg", fontSize = 10.sp, color = TextSecondary)
                    Text("${review.avgScore.toInt()}/300", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldDone)
                }
            }

            if (review.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Notes: ${review.notes}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun EditWeeklyReviewDialog(
    review: WeeklyReviewEntity,
    onDismiss: () -> Unit,
    onSave: (WeeklyReviewEntity) -> Unit
) {
    var hoursText by remember { mutableStateOf(if (review.studyHours > 0) review.studyHours.toString() else "") }
    var lecturesText by remember { mutableStateOf(if (review.lecturesDone > 0) review.lecturesDone.toString() else "") }
    var dppText by remember { mutableStateOf(if (review.dppDone > 0) review.dppDone.toString() else "") }
    var pyqText by remember { mutableStateOf(if (review.pyqDone > 0) review.pyqDone.toString() else "") }
    var testsText by remember { mutableStateOf(if (review.testsDone > 0) review.testsDone.toString() else "") }
    var avgScoreText by remember { mutableStateOf(if (review.avgScore > 0) review.avgScore.toInt().toString() else "") }
    var consistency by remember { mutableIntStateOf(review.consistency) }
    var notesText by remember { mutableStateOf(review.notes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepSpaceSlate,
        title = {
            Text("Update Week ${review.weekNumber} Metrics", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = hoursText,
                        onValueChange = { hoursText = it },
                        label = { Text("Study Hours") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = lecturesText,
                        onValueChange = { lecturesText = it },
                        label = { Text("Lectures") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = dppText,
                        onValueChange = { dppText = it },
                        label = { Text("DPPs") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = pyqText,
                        onValueChange = { pyqText = it },
                        label = { Text("PYQs") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = testsText,
                        onValueChange = { testsText = it },
                        label = { Text("Tests") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = avgScoreText,
                        onValueChange = { avgScoreText = it },
                        label = { Text("Avg Score /300") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Consistency Rating: $consistency/10", fontSize = 12.sp, color = TextPrimary)
                Slider(
                    value = consistency.toFloat(),
                    onValueChange = { consistency = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Weekly Notes & Takeaways") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = review.copy(
                        studyHours = hoursText.toFloatOrNull() ?: 0f,
                        lecturesDone = lecturesText.toIntOrNull() ?: 0,
                        dppDone = dppText.toIntOrNull() ?: 0,
                        pyqDone = pyqText.toIntOrNull() ?: 0,
                        testsDone = testsText.toIntOrNull() ?: 0,
                        avgScore = avgScoreText.toFloatOrNull() ?: 0f,
                        consistency = consistency,
                        notes = notesText
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
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
