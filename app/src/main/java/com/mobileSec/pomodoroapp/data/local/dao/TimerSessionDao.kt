package com.mobileSec.pomodoroapp.data.local.dao

import androidx.room.*
import com.mobileSec.pomodoroapp.data.local.entity.TimerSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerSessionDao {
    @Query("SELECT * FROM timer_sessions ORDER BY completed_at DESC")
    fun getAllSessions(): Flow<List<TimerSessionEntity>>

    @Insert
    suspend fun insertSession(session: TimerSessionEntity)

    @Query("SELECT COUNT(*) FROM timer_sessions WHERE session_type = 'work' AND DATE(completed_at/1000, 'unixepoch') = DATE('now')")
    suspend fun getTodayWorkSessions(): Int
}