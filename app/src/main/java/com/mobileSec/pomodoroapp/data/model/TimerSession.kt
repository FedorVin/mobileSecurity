package com.mobileSec.pomodoroapp.data.model

data class TimerSession(
    val id: Int = 0,
    val durationMinutes: Int,
    val completedAt: Long,
    val todoId: Int? = null,
    val sessionType: SessionType
)

enum class SessionType {
    WORK, SHORT_BREAK, LONG_BREAK
}