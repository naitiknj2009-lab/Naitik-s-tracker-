package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class CheckpointType {
    LECTURE, DPP, PYQ, REPLICA, NOTES, TEST, ANALYSIS
}

class JeeRepository(
    private val dao: JeeDao,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    val allChapters: Flow<List<ChapterEntity>> = dao.getAllChapters()
    val allTests: Flow<List<TestEntity>> = dao.getAllTests()
    val allWeeklyReviews: Flow<List<WeeklyReviewEntity>> = dao.getAllWeeklyReviews()

    init {
        externalScope.launch {
            checkAndSeedDatabase()
        }
    }

    private suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        if (dao.getChapterCount() == 0) {
            val allInitialChapters = InitialData.physicsChapters +
                    InitialData.chemistryChapters +
                    InitialData.mathematicsChapters
            dao.insertChapters(allInitialChapters)
        }

        if (dao.getTestCount() == 0) {
            dao.insertTests(InitialData.tests)
        }

        if (dao.getWeeklyReviewCount() == 0) {
            dao.insertWeeklyReviews(InitialData.weeklyReviews)
        }
    }

    fun getChaptersBySubject(subject: SubjectType): Flow<List<ChapterEntity>> =
        dao.getChaptersBySubject(subject)

    fun getDailyCheckIn(date: String): Flow<DailyCheckInEntity?> =
        dao.getDailyCheckIn(date)

    suspend fun cycleCheckpoint(chapterId: Int, checkpointType: CheckpointType) = withContext(Dispatchers.IO) {
        val chapter = dao.getChapterById(chapterId) ?: return@withContext
        val updated = when (checkpointType) {
            CheckpointType.LECTURE -> chapter.copy(lecture = chapter.lecture.next())
            CheckpointType.DPP -> chapter.copy(dpp = chapter.dpp.next())
            CheckpointType.PYQ -> chapter.copy(pyq = chapter.pyq.next())
            CheckpointType.REPLICA -> chapter.copy(replicaSheet = chapter.replicaSheet.next())
            CheckpointType.NOTES -> chapter.copy(summaryNotes = chapter.summaryNotes.next())
            CheckpointType.TEST -> chapter.copy(test = chapter.test.next())
            CheckpointType.ANALYSIS -> chapter.copy(testAnalysis = chapter.testAnalysis.next())
        }
        dao.updateChapter(updated)
    }

    suspend fun setCheckpointStatus(
        chapterId: Int,
        checkpointType: CheckpointType,
        status: CheckpointStatus
    ) = withContext(Dispatchers.IO) {
        val chapter = dao.getChapterById(chapterId) ?: return@withContext
        val updated = when (checkpointType) {
            CheckpointType.LECTURE -> chapter.copy(lecture = status)
            CheckpointType.DPP -> chapter.copy(dpp = status)
            CheckpointType.PYQ -> chapter.copy(pyq = status)
            CheckpointType.REPLICA -> chapter.copy(replicaSheet = status)
            CheckpointType.NOTES -> chapter.copy(summaryNotes = status)
            CheckpointType.TEST -> chapter.copy(test = status)
            CheckpointType.ANALYSIS -> chapter.copy(testAnalysis = status)
        }
        dao.updateChapter(updated)
    }

    suspend fun updateChapterRemarks(chapterId: Int, remarks: String) = withContext(Dispatchers.IO) {
        val chapter = dao.getChapterById(chapterId) ?: return@withContext
        dao.updateChapter(chapter.copy(statusRemarks = remarks))
    }

    suspend fun toggleChapterTopicFlags(chapterId: Int, isWeak: Boolean?, isStrong: Boolean?) = withContext(Dispatchers.IO) {
        val chapter = dao.getChapterById(chapterId) ?: return@withContext
        dao.updateChapter(
            chapter.copy(
                isWeakTopic = isWeak ?: chapter.isWeakTopic,
                isStrongTopic = isStrong ?: chapter.isStrongTopic
            )
        )
    }

    suspend fun markAllCheckpoints(chapterId: Int, status: CheckpointStatus) = withContext(Dispatchers.IO) {
        val chapter = dao.getChapterById(chapterId) ?: return@withContext
        dao.updateChapter(
            chapter.copy(
                lecture = status,
                dpp = status,
                pyq = status,
                replicaSheet = status,
                summaryNotes = status,
                test = status,
                testAnalysis = status
            )
        )
    }

    suspend fun updateTest(test: TestEntity) = withContext(Dispatchers.IO) {
        dao.updateTest(test)
    }

    suspend fun saveDailyCheckIn(checkIn: DailyCheckInEntity) = withContext(Dispatchers.IO) {
        dao.insertOrUpdateDailyCheckIn(checkIn)
    }

    suspend fun updateWeeklyReview(review: WeeklyReviewEntity) = withContext(Dispatchers.IO) {
        dao.updateWeeklyReview(review)
    }

    suspend fun resetAllData() = withContext(Dispatchers.IO) {
        val allInitialChapters = InitialData.physicsChapters +
                InitialData.chemistryChapters +
                InitialData.mathematicsChapters
        dao.deleteAllChapters()
        dao.deleteAllTests()
        dao.deleteAllWeeklyReviews()
        dao.insertChapters(allInitialChapters)
        dao.insertTests(InitialData.tests)
        dao.insertWeeklyReviews(InitialData.weeklyReviews)
    }
}
