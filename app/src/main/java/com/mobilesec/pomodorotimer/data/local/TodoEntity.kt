package com.mobilesec.pomodorotimer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "timer_sessions")
data class TimerSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val duration: Int,
    val type: String,
    val completedAt: Long = System.currentTimeMillis()
)

