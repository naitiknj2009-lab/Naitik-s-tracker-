package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppTab(val label: String) {
    DASHBOARD("Overview"),
    PHYSICS("Physics"),
    CHEMISTRY("Chemistry"),
    MATHEMATICS("Maths"),
    TESTS("Test Series"),
    DAILY_LOG("Daily Log")
}

enum class ChapterFilter(val label: String) {
    ALL("All"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    NEEDS_REVISION("Revise Again"),
    PENDING("Not Started")
}

enum class TestTabFilter(val label: String) {
    ALL("All (32)"),
    PART_TESTS("Part Tests (1-12)"),
    AITS_MAINS("AITS Mains (13-29)"),
    AITS_ADVANCED("AITS Advanced (30-32)"),
    ATTEMPTED("Attempted"),
    UNATTEMPTED("Pending")
}

data class SubjectStats(
    val totalChapters: Int,
    val completedChapters: Int,
    val completedCheckpoints: Int,
    val totalCheckpoints: Int,
    val progressFraction: Float,
    val revisionCount: Int,
    val strongestTopic: String,
    val weakTopicsCount: Int
)

data class DashboardOverview(
    val physicsStats: SubjectStats,
    val chemistryStats: SubjectStats,
    val mathStats: SubjectStats,
    val overallProgress: Float,
    val totalChapters: Int,
    val totalCompletedChapters: Int,
    val testsAttempted: Int,
    val totalTests: Int,
    val bestScore: Int,
    val avgScore: Float,
    val testsAnalyzed: Int
)

class JeeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: JeeRepository

    init {
        val db = JeeDatabase.getDatabase(application)
        repository = JeeRepository(db.jeeDao(), viewModelScope)
    }

    private val _selectedTab = MutableStateFlow(AppTab.DASHBOARD)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _chapterFilter = MutableStateFlow(ChapterFilter.ALL)
    val chapterFilter: StateFlow<ChapterFilter> = _chapterFilter.asStateFlow()

    private val _testFilter = MutableStateFlow(TestTabFilter.ALL)
    val testFilter: StateFlow<TestTabFilter> = _testFilter.asStateFlow()

    val allChapters: StateFlow<List<ChapterEntity>> = repository.allChapters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTests: StateFlow<List<TestEntity>> = repository.allTests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weeklyReviews: StateFlow<List<WeeklyReviewEntity>> = repository.allWeeklyReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val todayCheckIn: StateFlow<DailyCheckInEntity?> = repository.getDailyCheckIn(todayDateString)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Filtered chapters for current tab
    val currentSubjectChapters: StateFlow<List<ChapterEntity>> = combine(
        allChapters,
        _selectedTab,
        _searchQuery,
        _chapterFilter
    ) { chapters, tab, query, filter ->
        val subject = when (tab) {
            AppTab.PHYSICS -> SubjectType.PHYSICS
            AppTab.CHEMISTRY -> SubjectType.CHEMISTRY
            AppTab.MATHEMATICS -> SubjectType.MATHEMATICS
            else -> null
        }
        val subjectFiltered = if (subject != null) {
            chapters.filter { it.subject == subject }
        } else {
            chapters
        }

        subjectFiltered.filter { chapter ->
            val matchesQuery = query.isBlank() || chapter.name.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                ChapterFilter.ALL -> true
                ChapterFilter.IN_PROGRESS -> chapter.completedCount in 1..6
                ChapterFilter.COMPLETED -> chapter.completedCount == 7
                ChapterFilter.NEEDS_REVISION -> chapter.hasRevision
                ChapterFilter.PENDING -> chapter.completedCount == 0 && !chapter.hasRevision
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Tests
    val filteredTests: StateFlow<List<TestEntity>> = combine(
        allTests,
        _testFilter
    ) { tests, filter ->
        when (filter) {
            TestTabFilter.ALL -> tests
            TestTabFilter.PART_TESTS -> tests.filter { it.sNo in 1..12 }
            TestTabFilter.AITS_MAINS -> tests.filter { it.sNo in 13..29 }
            TestTabFilter.AITS_ADVANCED -> tests.filter { it.sNo in 30..32 }
            TestTabFilter.ATTEMPTED -> tests.filter { it.attempted }
            TestTabFilter.UNATTEMPTED -> tests.filter { !it.attempted }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dashboard Overview stats
    val dashboardOverview: StateFlow<DashboardOverview> = combine(
        allChapters,
        allTests
    ) { chapters, tests ->
        fun computeSubjectStats(subject: SubjectType): SubjectStats {
            val list = chapters.filter { it.subject == subject }
            val totalChapters = list.size
            val completedChapters = list.count { it.completedCount == 7 }
            val completedCheckpoints = list.sumOf { it.completedCount }
            val totalCheckpoints = totalChapters * 7
            val progressFraction = if (totalCheckpoints > 0) completedCheckpoints.toFloat() / totalCheckpoints else 0f
            val revisionCount = list.count { it.hasRevision }
            val strongest = list.firstOrNull { it.isStrongTopic }?.name
                ?: list.maxByOrNull { it.completedCount }?.name
                ?: "To be decided"
            val weakCount = list.count { it.isWeakTopic }

            return SubjectStats(
                totalChapters = totalChapters,
                completedChapters = completedChapters,
                completedCheckpoints = completedCheckpoints,
                totalCheckpoints = totalCheckpoints,
                progressFraction = progressFraction,
                revisionCount = revisionCount,
                strongestTopic = strongest,
                weakTopicsCount = weakCount
            )
        }

        val phyStats = computeSubjectStats(SubjectType.PHYSICS)
        val chemStats = computeSubjectStats(SubjectType.CHEMISTRY)
        val mathStats = computeSubjectStats(SubjectType.MATHEMATICS)

        val totalCheckpoints = chapters.size * 7
        val totalCompletedCheckpoints = chapters.sumOf { it.completedCount }
        val overallProgress = if (totalCheckpoints > 0) totalCompletedCheckpoints.toFloat() / totalCheckpoints else 0f
        val totalCompletedChapters = chapters.count { it.completedCount == 7 }

        val attemptedTests = tests.filter { it.attempted }
        val testsAttempted = attemptedTests.size
        val bestScore = attemptedTests.maxOfOrNull { it.score } ?: 0
        val avgScore = if (attemptedTests.isNotEmpty()) attemptedTests.map { it.score }.average().toFloat() else 0f
        val testsAnalyzed = tests.count { it.analysisDone }

        DashboardOverview(
            physicsStats = phyStats,
            chemistryStats = chemStats,
            mathStats = mathStats,
            overallProgress = overallProgress,
            totalChapters = chapters.size,
            totalCompletedChapters = totalCompletedChapters,
            testsAttempted = testsAttempted,
            totalTests = tests.size,
            bestScore = bestScore,
            avgScore = avgScore,
            testsAnalyzed = testsAnalyzed
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardOverview(
            physicsStats = SubjectStats(29, 0, 0, 203, 0f, 0, "Units & Dimensions", 0),
            chemistryStats = SubjectStats(22, 0, 0, 154, 0f, 0, "Structure of Atom", 0),
            mathStats = SubjectStats(28, 0, 0, 196, 0f, 0, "Sequence & Series", 0),
            overallProgress = 0f,
            totalChapters = 79,
            totalCompletedChapters = 0,
            testsAttempted = 0,
            totalTests = 32,
            bestScore = 0,
            avgScore = 0f,
            testsAnalyzed = 0
        )
    )

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setChapterFilter(filter: ChapterFilter) {
        _chapterFilter.value = filter
    }

    fun setTestFilter(filter: TestTabFilter) {
        _testFilter.value = filter
    }

    fun cycleCheckpoint(chapterId: Int, checkpointType: CheckpointType) {
        viewModelScope.launch {
            repository.cycleCheckpoint(chapterId, checkpointType)
        }
    }

    fun setCheckpointStatus(chapterId: Int, checkpointType: CheckpointType, status: CheckpointStatus) {
        viewModelScope.launch {
            repository.setCheckpointStatus(chapterId, checkpointType, status)
        }
    }

    fun updateChapterRemarks(chapterId: Int, remarks: String) {
        viewModelScope.launch {
            repository.updateChapterRemarks(chapterId, remarks)
        }
    }

    fun toggleWeakTopic(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.toggleChapterTopicFlags(chapter.id, isWeak = !chapter.isWeakTopic, isStrong = null)
        }
    }

    fun toggleStrongTopic(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.toggleChapterTopicFlags(chapter.id, isWeak = null, isStrong = !chapter.isStrongTopic)
        }
    }

    fun markAllCheckpoints(chapterId: Int, status: CheckpointStatus) {
        viewModelScope.launch {
            repository.markAllCheckpoints(chapterId, status)
        }
    }

    fun updateTest(test: TestEntity) {
        viewModelScope.launch {
            repository.updateTest(test)
        }
    }

    fun saveTodayCheckIn(
        whatCompleted: String,
        whatNotCompleted: String,
        whyDistraction: String,
        task1: String,
        task2: String,
        task3: String,
        backlog: String,
        confidence: Int,
        energy: Int,
        sleepHours: Float
    ) {
        viewModelScope.launch {
            val checkIn = DailyCheckInEntity(
                date = todayDateString,
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
            repository.saveDailyCheckIn(checkIn)
        }
    }

    fun updateWeeklyReview(review: WeeklyReviewEntity) {
        viewModelScope.launch {
            repository.updateWeeklyReview(review)
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetAllData()
        }
    }
}
