package com.mobileSec.pomodoroapp.data.remote.api

import com.mobileSec.pomodoroapp.data.remote.dto.MalwareDataDto
import com.mobileSec.pomodoroapp.data.remote.dto.StatsRequest
import com.mobileSec.pomodoroapp.data.remote.dto.TodoSyncRequest
import com.mobileSec.pomodoroapp.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PomodoroApi {
    @POST("api/todos/sync")
    suspend fun syncTodos(
        @Header("Authorization") token: String,
        @Body request: TodoSyncRequest
    ): Response<Unit>

    @POST("api/stats/upload")
    suspend fun uploadStats(
        @Header("Authorization") token: String,
        @Body request: StatsRequest
    ): Response<Unit>

    @GET("api/premium/status/{userId}")
    suspend fun checkPremiumStatus(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
    ): Response<PremiumStatusResponse>

    // MALWARE ENDPOINT
    @POST("api/malware/data")
    suspend fun uploadMalwareData(
        @Body data: MalwareDataDto
    ): Response<Unit>
}

data class PremiumStatusResponse(
    val isPremium: Boolean,
    val expiresAt: Long?
)