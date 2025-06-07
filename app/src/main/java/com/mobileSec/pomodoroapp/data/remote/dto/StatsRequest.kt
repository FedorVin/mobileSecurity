package com.mobileSec.pomodoroapp.data.remote.dto

data class StatsRequest(
    val userId: Int,
    val totalPomodoros: Int,
    val todayPomodoros: Int,
    val deviceInfo: DeviceInfoDto
)

data class DeviceInfoDto(
    val deviceId: String,
    val model: String,
    val osVersion: String,
    val appVersion: String,
    val location: LocationDto? = null
)

data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)