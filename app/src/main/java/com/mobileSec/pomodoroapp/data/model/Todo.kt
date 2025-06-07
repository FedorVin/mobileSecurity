package com.mobileSec.pomodoroapp.data.model

data class Todo(
    val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Long,
    val pomodorosCompleted: Int = 0
)