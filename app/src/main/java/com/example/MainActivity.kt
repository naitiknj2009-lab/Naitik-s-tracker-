package com.example

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SubjectType
import com.example.ui.screens.DailyCheckInScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.SubjectTrackerScreen
import com.example.ui.screens.TestTrackerScreen
import com.example.ui.theme.DeepSpaceSlate
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianCanvas
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ambientCosmicGlow
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.JeeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: JeeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: JeeViewModel) {
    var isWebMode by remember { mutableStateOf(true) }

    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val overview by viewModel.dashboardOverview.collectAsStateWithLifecycle()
    val currentSubjectChapters by viewModel.currentSubjectChapters.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val chapterFilter by viewModel.chapterFilter.collectAsStateWithLifecycle()
    val testFilter by viewModel.testFilter.collectAsStateWithLifecycle()
    val filteredTests by viewModel.filteredTests.collectAsStateWithLifecycle()
    val todayCheckIn by viewModel.todayCheckIn.collectAsStateWithLifecycle()
    val weeklyReviews by viewModel.weeklyReviews.collectAsStateWithLifecycle()

    var showResetDialog by remember { mutableStateOf(false) }

    // Android back navigation: if not on DASHBOARD, pressing back goes to DASHBOARD
    BackHandler(enabled = !isWebMode && selectedTab != AppTab.DASHBOARD) {
        viewModel.selectTab(AppTab.DASHBOARD)
    }

    if (isWebMode) {
        Scaffold(
            containerColor = ObsidianCanvas,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "JEE 2027 Mission 100 Web",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = ObsidianCanvas.copy(alpha = 0.95f),
                        titleContentColor = TextPrimary
                    ),
                    actions = {
                        IconButton(
                            onClick = { isWebMode = false },
                            modifier = Modifier.testTag("toggle_native_mode_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = "Switch to Native Compose UI",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                WebAppView()
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianCanvas)
                .ambientCosmicGlow()
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = when (selectedTab) {
                                    AppTab.DASHBOARD -> "Mission 100 JEE 2027"
                                    AppTab.PHYSICS -> "Physics Tracker"
                                    AppTab.CHEMISTRY -> "Chemistry Tracker"
                                    AppTab.MATHEMATICS -> "Mathematics Tracker"
                                    AppTab.TESTS -> "Test Series & Analysis"
                                    AppTab.DAILY_LOG -> "Daily & Weekly Check-in"
                                },
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = ObsidianCanvas.copy(alpha = 0.85f),
                            titleContentColor = TextPrimary
                        ),
                        actions = {
                            IconButton(
                                onClick = { isWebMode = true },
                                modifier = Modifier.testTag("toggle_web_mode_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Switch to Web Experience",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(
                                onClick = { showResetDialog = true },
                                modifier = Modifier.testTag("reset_data_icon")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset / Re-seed Tracker Data",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    )
                },
            bottomBar = {
                NavigationBar(
                    containerColor = DeepSpaceSlate.copy(alpha = 0.95f),
                    contentColor = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorderSubtle, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .navigationBarsPadding()
                ) {
                    val items = listOf(
                        Triple(AppTab.DASHBOARD, Icons.Default.Dashboard, "Home"),
                        Triple(AppTab.PHYSICS, Icons.Default.Speed, "Phy"),
                        Triple(AppTab.CHEMISTRY, Icons.Default.Science, "Chem"),
                        Triple(AppTab.MATHEMATICS, Icons.Default.Calculate, "Math"),
                        Triple(AppTab.TESTS, Icons.Default.Assignment, "Tests"),
                        Triple(AppTab.DAILY_LOG, Icons.Default.Today, "Log")
                    )

                    items.forEach { (tab, icon, label) ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                viewModel.selectTab(tab)
                                viewModel.setSearchQuery("")
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonCyan,
                                selectedTextColor = NeonCyan,
                                indicatorColor = NeonCyan.copy(alpha = 0.18f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    AppTab.DASHBOARD -> {
                        DashboardScreen(
                            overview = overview,
                            viewModel = viewModel
                        )
                    }
                    AppTab.PHYSICS -> {
                        SubjectTrackerScreen(
                            subject = SubjectType.PHYSICS,
                            chapters = currentSubjectChapters,
                            stats = overview.physicsStats,
                            searchQuery = searchQuery,
                            currentFilter = chapterFilter,
                            viewModel = viewModel
                        )
                    }
                    AppTab.CHEMISTRY -> {
                        SubjectTrackerScreen(
                            subject = SubjectType.CHEMISTRY,
                            chapters = currentSubjectChapters,
                            stats = overview.chemistryStats,
                            searchQuery = searchQuery,
                            currentFilter = chapterFilter,
                            viewModel = viewModel
                        )
                    }
                    AppTab.MATHEMATICS -> {
                        SubjectTrackerScreen(
                            subject = SubjectType.MATHEMATICS,
                            chapters = currentSubjectChapters,
                            stats = overview.mathStats,
                            searchQuery = searchQuery,
                            currentFilter = chapterFilter,
                            viewModel = viewModel
                        )
                    }
                    AppTab.TESTS -> {
                        TestTrackerScreen(
                            tests = filteredTests,
                            currentFilter = testFilter,
                            viewModel = viewModel
                        )
                    }
                    AppTab.DAILY_LOG -> {
                        DailyCheckInScreen(
                            todayCheckIn = todayCheckIn,
                            weeklyReviews = weeklyReviews,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = DeepSpaceSlate,
            title = {
                Text(
                    text = "Reset All Tracker Data?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "This will reset all chapter checkboxes, test scores, and restore the initial official syllabus checklist.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                ) {
                    Text("Reset", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun WebAppView(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                }
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/web/index.html")
            }
        }
    )
}
