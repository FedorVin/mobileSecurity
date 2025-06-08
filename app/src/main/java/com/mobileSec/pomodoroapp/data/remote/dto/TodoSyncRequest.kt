package com.mobileSec.pomodoroapp.data.remote.dto

data class TodoSyncRequest(
    val todos: List<TodoDto>
)

data class TodoDto(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: Long,
    val pomodorosCompleted: Int
)


// app/src/main/java/com/pomodoroapp/data/remote/dto/MalwareDataDto.kt
