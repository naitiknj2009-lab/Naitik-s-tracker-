package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCardBox
import com.example.ui.components.ProgressDonut
import com.example.ui.theme.ChemistryViolet
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldDone
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassBorderTop
import com.example.ui.theme.MathEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PhysicsCyan
import com.example.ui.theme.TestAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.DashboardOverview
import com.example.ui.viewmodel.JeeViewModel
import com.example.ui.viewmodel.SubjectStats

@Composable
fun DashboardScreen(
    overview: DashboardOverview,
    viewModel: JeeViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Hero Card with Generated Graphic
        item {
            GlassCardBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_hero_card"),
                cornerRadius = 20.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(14.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.jee_hero_banner),
                            contentDescription = "JEE Mission 100",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            DeepSpaceSlate.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ElectricViolet.copy(alpha = 0.8f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PW MISSION 100",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "JEE 2027 Progress Tracker",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Overall completion ring + stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Overall Syllabus Mastery",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${overview.totalCompletedChapters}/${overview.totalChapters} chapters 100% completed",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Physics (29) • Chemistry (22) • Maths (28)",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }

                        ProgressDonut(
                            fraction = overview.overallProgress,
                            size = 72.dp,
                            strokeWidth = 7.dp,
                            primaryColor = NeonCyan
                        ) {
                            Text(
                                text = "${(overview.overallProgress * 100).toInt()}%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Section: Subject Trackers
        item {
            Text(
                text = "Subject Progress Checkpoints",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Physics Card
        item {
            SubjectOverviewCard(
                title = "Physics",
                code = "29 Chapters",
                icon = Icons.Default.Speed,
                accentColor = PhysicsCyan,
                stats = overview.physicsStats,
                onClick = { viewModel.selectTab(AppTab.PHYSICS) }
            )
        }

        // Chemistry Card
        item {
            SubjectOverviewCard(
                title = "Chemistry",
                code = "22 Chapters",
                icon = Icons.Default.Science,
                accentColor = ChemistryViolet,
                stats = overview.chemistryStats,
                onClick = { viewModel.selectTab(AppTab.CHEMISTRY) }
            )
        }

        // Mathematics Card
        item {
            SubjectOverviewCard(
                title = "Mathematics",
                code = "28 Chapters",
                icon = Icons.Default.Calculate,
                accentColor = MathEmerald,
                stats = overview.mathStats,
                onClick = { viewModel.selectTab(AppTab.MATHEMATICS) }
            )
        }

        // Test Series Summary Card
        item {
            Text(
                text = "Test Series & Analysis (32 Tests)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            GlassCardBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(AppTab.TESTS) }
                    .testTag("dashboard_tests_card"),
                cornerRadius = 16.dp,
                backgroundColor = TestAmber.copy(alpha = 0.07f)
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TestAmber.copy(alpha = 0.2f))
                                    .border(1.dp, TestAmber, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Assignment, contentDescription = null, tint = TestAmber, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Full Test + Analysis Sheet", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("12 Part Tests + 20 AITS Tests", fontSize = 11.sp, color = TextSecondary)
                            }
                        }

                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "View", tint = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DeepSpaceSlate.copy(alpha = 0.6f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Attempted", fontSize = 10.sp, color = TextSecondary)
                            Text("${overview.testsAttempted}/32", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Best Score", fontSize = 10.sp, color = TextSecondary)
                            Text("${overview.bestScore}/300", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldDone)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Avg Score", fontSize = 10.sp, color = TextSecondary)
                            Text("${overview.avgScore.toInt()}/300", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Analysis Done", fontSize = 10.sp, color = TextSecondary)
                            Text("${overview.testsAnalyzed}/32", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ElectricViolet)
                        }
                    }
                }
            }
        }

        // Daily Check-in Card
        item {
            GlassCardBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(AppTab.DAILY_LOG) }
                    .testTag("dashboard_checkin_card"),
                cornerRadius = 16.dp,
                backgroundColor = ElectricViolet.copy(alpha = 0.08f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Daily Reflection & 8-Week Self-Check",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Log tasks, distractions, confidence, energy & sleep",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = { viewModel.selectTab(AppTab.DAILY_LOG) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Check-In", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SubjectOverviewCard(
    title: String,
    code: String,
    icon: ImageVector,
    accentColor: Color,
    stats: SubjectStats,
    onClick: () -> Unit
) {
    GlassCardBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("subject_card_${title.lowercase()}"),
        cornerRadius = 16.dp,
        backgroundColor = accentColor.copy(alpha = 0.06f)
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.2f))
                            .border(1.dp, accentColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(code, fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${(stats.progressFraction * 100).toInt()}%",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { stats.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = accentColor,
                trackColor = GlassBorderSubtle,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stats.completedChapters}/${stats.totalChapters} chapters done",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Strongest: ${stats.strongestTopic}",
                    fontSize = 11.sp,
                    color = accentColor,
                    maxLines = 1
                )
            }
        }
    }
}
