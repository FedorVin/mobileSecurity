package com.mobileSec.pomodoroapp.data.local.dao

import androidx.room.*
import com.mobileSec.pomodoroapp.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET is_premium = :isPremium WHERE id = :userId")
    suspend fun updatePremiumStatus(userId: Int, isPremium: Boolean)
}