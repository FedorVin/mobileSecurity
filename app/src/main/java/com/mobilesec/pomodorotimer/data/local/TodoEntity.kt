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
    val duration: Int, // in minutes
    val type: String, // "work" or "break"
    val completedAt: Long = System.currentTimeMillis()
)

//// Vulnerable: Plain text credentials storage
@Entity(tableName = "user_credentials")
data class UserCredentialsEntity(
    @PrimaryKey
    val id: Int = 1,
    val username: String,
    val password: String, // VULNERABILITY: Plain text password storage
    val apiKey: String = "secret_api_key_123", // VULNERABILITY: Hardcoded API key
    val isPremium: Boolean = false // Can be bypassed with Frida
)
