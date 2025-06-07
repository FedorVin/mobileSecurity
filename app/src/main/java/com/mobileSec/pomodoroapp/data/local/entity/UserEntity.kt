package com.mobileSec.pomodoroapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String, // VULNERABILITY: Plain text password
    @ColumnInfo(name = "is_premium")
    val isPremium: Boolean = false,
    @ColumnInfo(name = "api_key")
    val apiKey: String? = null
)