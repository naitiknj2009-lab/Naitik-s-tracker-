package com.example.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromSubjectType(value: SubjectType): String = value.name

    @TypeConverter
    fun toSubjectType(value: String): SubjectType = try {
        SubjectType.valueOf(value)
    } catch (e: Exception) {
        SubjectType.PHYSICS
    }

    @TypeConverter
    fun fromCheckpointStatus(value: CheckpointStatus): String = value.name

    @TypeConverter
    fun toCheckpointStatus(value: String): CheckpointStatus = try {
        CheckpointStatus.valueOf(value)
    } catch (e: Exception) {
        CheckpointStatus.PENDING
    }
}
