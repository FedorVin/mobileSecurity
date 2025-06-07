package com.mobileSec.pomodoroapp.repository

import com.mobileSec.pomodoroapp.data.local.dao.UserDao
import com.mobileSec.pomodoroapp.data.mapper.toDomainModel
import com.mobileSec.pomodoroapp.data.model.User
import com.mobileSec.pomodoroapp.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    private val api = RetrofitClient.api

    suspend fun login(email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val userEntity = userDao.login(email, password)
                if (userEntity != null) {
                    Result.success(userEntity.toDomainModel())
                } else {
                    Result.failure(Exception("Invalid credentials"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun checkPremiumStatus(userId: Int, token: String): Boolean {
        return try {
            val response = api.checkPremiumStatus(userId, "Bearer $token")
            if (response.isSuccessful) {
                response.body()?.isPremium ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updatePremiumStatus(userId: Int, isPremium: Boolean) {
        userDao.updatePremiumStatus(userId, isPremium)
    }
}