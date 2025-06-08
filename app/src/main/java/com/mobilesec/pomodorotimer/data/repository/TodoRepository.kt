package com.mobilesec.pomodorotimer.data.repository

import com.mobilesec.pomodorotimer.data.local.*
import com.mobilesec.pomodorotimer.data.remote.*
import kotlinx.coroutines.flow.Flow

class TodoRepository(
    private val todoDao: TodoDao,
    private val timerSessionDao: TimerSessionDao,
    private val userCredentialsDao: UserCredentialsDao,
    private val apiService: ApiService
) {
    fun getAllTodos(): Flow<List<TodoEntity>> = todoDao.getAllTodos()

    suspend fun insertTodo(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    suspend fun insertTimerSession(session: TimerSessionEntity) {
        timerSessionDao.insertSession(session)
    }

    fun getAllSessions(): Flow<List<TimerSessionEntity>> = timerSessionDao.getAllSessions()

    suspend fun getWorkSessionsCount(since: Long): Int =
        timerSessionDao.getWorkSessionsCount(since)

    suspend fun syncTodos() {
        try {
            val todos = todoDao.getAllTodos()
            // Convert and sync with API
        } catch (e: Exception) {
            // Handle sync error
        }
    }

    suspend fun getCredentials(): UserCredentialsEntity? = userCredentialsDao.getCredentials()

    suspend fun saveCredentials(credentials: UserCredentialsEntity) {
        userCredentialsDao.saveCredentials(credentials)
    }

    suspend fun updatePremiumStatus(isPremium: Boolean) {
        userCredentialsDao.updatePremiumStatus(isPremium)
    }
}
