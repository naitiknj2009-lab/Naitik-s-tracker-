package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ChapterEntity::class,
        TestEntity::class,
        DailyCheckInEntity::class,
        WeeklyReviewEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JeeDatabase : RoomDatabase() {
    abstract fun jeeDao(): JeeDao

    companion object {
        @Volatile
        private var INSTANCE: JeeDatabase? = null

        fun getDatabase(context: Context): JeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JeeDatabase::class.java,
                    "jee_tracker_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
