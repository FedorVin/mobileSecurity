package com.mobilesec.pomodorotimer.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Insert
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodoById(id: Int)
}

@Dao
interface TimerSessionDao {
    @Query("SELECT * FROM timer_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<TimerSessionEntity>>

    @Insert
    suspend fun insertSession(session: TimerSessionEntity)

    @Query("SELECT COUNT(*) FROM timer_sessions WHERE type = 'work' AND completedAt > :since")
    suspend fun getWorkSessionsCount(since: Long): Int
}

//@Dao
//interface UserSettingsDao {
//    @Query("SELECT * FROM user_settings WHERE id = 1")
//    suspend fun getSettings(): UserSettingsEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveSettings(settings: UserSettingsEntity)
//
//    @Query("UPDATE user_settings SET isPremium = :isPremium WHERE id = 1")
//    suspend fun updatePremiumStatus(isPremium: Boolean)
//}

//@Dao
//interface UserCredentialsDao {
//    @Query("SELECT * FROM user_credentials WHERE id = 1")
//    suspend fun getCredentials(): UserCredentialsEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveSettings(credentials: UserCredentialsEntity)
//
//    @Query("UPDATE user_credentials SET isPremium = :isPremium WHERE id = 1")
//    suspend fun updatePremiumStatus(isPremium: Boolean)
//}

