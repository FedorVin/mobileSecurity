package com.mobileSec.pomodoroapp.data.model

data class User(
    val id: Int,
    val email: String,
    val isPremium: Boolean = false,
    val apiKey: String? = null
)