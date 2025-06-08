package com.mobileSec.pomodoroapp.repository

import com.mobileSec.pomodoroapp.data.local.dao.TimerSessionDao
import com.mobileSec.pomodoroapp.data.mapper.toDomainModel
import com.mobileSec.pomodoroapp.data.mapper.toEntity
import com.mobileSec.pomodoroapp.data.model.TimerSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerRepository @Inject constructor(
    private val timerSessionDao: TimerSessionDao
) {
//    fun getAllSessions(): Flow<List<TimerSession>> {
//        return timerSessionDao.getAllSessions().map { entities ->
//            entities.map { com.mobileSec.pomodoroapp.data.mapper.toDomainModel() }
//        }
//    }

    suspend fun saveSession(session: TimerSession) {
        timerSessionDao.insertSession(session.toEntity())
    }

    suspend fun getTodayWorkSessions(): Int {
        return timerSessionDao.getTodayWorkSessions()
    }
}
