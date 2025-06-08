package com.mobileSec.pomodoroapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "timer_sessions")
data class TimerSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int,
    @ColumnInfo(name = "completed_at")
    val completedAt: Long,
    @ColumnInfo(name = "todo_id")
    val todoId: Int? = null,
    @ColumnInfo(name = "session_type")
    val sessionType: String // "work", "short_break", "long_break"
)
