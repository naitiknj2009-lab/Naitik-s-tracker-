package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JeeDao {
    // Chapters
    @Query("SELECT * FROM chapters ORDER BY sNo ASC")
    fun getAllChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE subject = :subject ORDER BY sNo ASC")
    fun getChaptersBySubject(subject: SubjectType): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :id")
    suspend fun getChapterById(id: Int): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("SELECT COUNT(*) FROM chapters")
    suspend fun getChapterCount(): Int

    @Query("DELETE FROM chapters")
    suspend fun deleteAllChapters()

    // Tests
    @Query("SELECT * FROM tests ORDER BY sNo ASC")
    fun getAllTests(): Flow<List<TestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTests(tests: List<TestEntity>)

    @Update
    suspend fun updateTest(test: TestEntity)

    @Query("SELECT COUNT(*) FROM tests")
    suspend fun getTestCount(): Int

    @Query("DELETE FROM tests")
    suspend fun deleteAllTests()

    // Daily Check-in
    @Query("SELECT * FROM daily_checkins WHERE date = :date")
    fun getDailyCheckIn(date: String): Flow<DailyCheckInEntity?>

    @Query("SELECT * FROM daily_checkins ORDER BY date DESC")
    fun getAllDailyCheckIns(): Flow<List<DailyCheckInEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyCheckIn(checkIn: DailyCheckInEntity)

    // Weekly Reviews
    @Query("SELECT * FROM weekly_reviews ORDER BY weekNumber ASC")
    fun getAllWeeklyReviews(): Flow<List<WeeklyReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyReviews(reviews: List<WeeklyReviewEntity>)

    @Update
    suspend fun updateWeeklyReview(review: WeeklyReviewEntity)

    @Query("SELECT COUNT(*) FROM weekly_reviews")
    suspend fun getWeeklyReviewCount(): Int

    @Query("DELETE FROM weekly_reviews")
    suspend fun deleteAllWeeklyReviews()
}
