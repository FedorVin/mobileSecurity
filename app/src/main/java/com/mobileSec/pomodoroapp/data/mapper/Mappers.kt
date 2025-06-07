package com.mobileSec.pomodoroapp.data.mapper

import com.mobileSec.pomodoroapp.data.local.entity.TimerSessionEntity
import com.mobileSec.pomodoroapp.data.local.entity.UserEntity
import com.mobileSec.pomodoroapp.data.model.SessionType
import com.mobileSec.pomodoroapp.data.model.TimerSession
import com.mobileSec.pomodoroapp.data.model.Todo
import com.mobileSec.pomodoroapp.data.model.User
import com.mobileSec.pomodoroapp.data.remote.dto.TodoDto
import com.mobileSec.pomodoroapp.data.local.entity.*
import com.mobileSec.pomodoroapp.data.model.*
import com.mobileSec.pomodoroapp.data.remote.dto.*

// Todo mappers
fun TodoEntity.toDomainModel(): Todo {
    return Todo(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted,
        createdAt = this.createdAt,
        pomodorosCompleted = this.pomodorosCompleted
    )
}

fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted,
        createdAt = this.createdAt,
        pomodorosCompleted = this.pomodorosCompleted
    )
}

fun Todo.toDto(): TodoDto {
    return TodoDto(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted,
        createdAt = this.createdAt,
        pomodorosCompleted = this.pomodorosCompleted
    )
}

// Timer session mappers
fun TimerSessionEntity.toDomainModel(): TimerSession {
    return TimerSession(
        id = this.id,
        durationMinutes = this.durationMinutes,
        completedAt = this.completedAt,
        todoId = this.todoId,
        sessionType = SessionType.valueOf(this.sessionType.uppercase())
    )
}

fun TimerSession.toEntity(): TimerSessionEntity {
    return TimerSessionEntity(
        id = this.id,
        durationMinutes = this.durationMinutes,
        completedAt = this.completedAt,
        todoId = this.todoId,
        sessionType = this.sessionType.name.lowercase()
    )
}

// User mappers
fun UserEntity.toDomainModel(): User {
    return User(
        id = this.id,
        email = this.email,
        isPremium = this.isPremium,
        apiKey = this.apiKey
    )
}