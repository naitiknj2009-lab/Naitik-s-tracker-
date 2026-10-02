package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SubjectType(val displayName: String, val code: String) {
    PHYSICS("Physics", "PHY"),
    CHEMISTRY("Chemistry", "CHEM"),
    MATHEMATICS("Mathematics", "MATH")
}

enum class CheckpointStatus {
    PENDING,    // ■
    DONE,       // ✓
    REVISE;     // →

    fun next(): CheckpointStatus = when (this) {
        PENDING -> DONE
        DONE -> REVISE
        REVISE -> PENDING
    }
}

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: SubjectType,
    val sNo: Int,
    val name: String,
    val lecture: CheckpointStatus = CheckpointStatus.PENDING,
    val dpp: CheckpointStatus = CheckpointStatus.PENDING,
    val pyq: CheckpointStatus = CheckpointStatus.PENDING,
    val replicaSheet: CheckpointStatus = CheckpointStatus.PENDING,
    val summaryNotes: CheckpointStatus = CheckpointStatus.PENDING,
    val test: CheckpointStatus = CheckpointStatus.PENDING,
    val testAnalysis: CheckpointStatus = CheckpointStatus.PENDING,
    val statusRemarks: String = "",
    val isWeakTopic: Boolean = false,
    val isStrongTopic: Boolean = false
)

val ChapterEntity.completedCount: Int
    get() = listOf(lecture, dpp, pyq, replicaSheet, summaryNotes, test, testAnalysis)
        .count { it == CheckpointStatus.DONE }

val ChapterEntity.totalCheckpoints: Int
    get() = 7

val ChapterEntity.progressFraction: Float
    get() = completedCount.toFloat() / 7f

val ChapterEntity.hasRevision: Boolean
    get() = listOf(lecture, dpp, pyq, replicaSheet, summaryNotes, test, testAnalysis)
        .any { it == CheckpointStatus.REVISE }

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sNo: Int,
    val name: String,
    val type: String, // "Part Test", "Full Test"
    val pattern: String, // "JEE Mains", "Main", "JEE Advanced"
    val date: String,
    val attempted: Boolean = false,
    val score: Int = 0, // out of 300
    val totalMarks: Int = 300,
    val accuracy: Float = 0f, // percentage e.g. 75.0
    val mistakesTopics: String = "",
    val analysisDone: Boolean = false
)

@Entity(tableName = "daily_checkins")
data class DailyCheckInEntity(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val whatCompleted: String = "",
    val whatNotCompleted: String = "",
    val whyDistraction: String = "",
    val task1: String = "",
    val task2: String = "",
    val task3: String = "",
    val backlog: String = "",
    val confidence: Int = 8, // /10
    val energy: Int = 8, // /10
    val sleepHours: Float = 7.0f
)

@Entity(tableName = "weekly_reviews")
data class WeeklyReviewEntity(
    @PrimaryKey val weekNumber: Int,
    val studyHours: Float = 0f,
    val lecturesDone: Int = 0,
    val dppDone: Int = 0,
    val pyqDone: Int = 0,
    val testsDone: Int = 0,
    val avgScore: Float = 0f,
    val consistency: Int = 8, // /10
    val notes: String = ""
)
