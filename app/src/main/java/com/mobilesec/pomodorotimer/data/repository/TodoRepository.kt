package com.mobilesec.pomodorotimer.data.repository

import android.util.Log
import com.mobilesec.pomodorotimer.data.local.*
import com.mobilesec.pomodorotimer.data.remote.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class TodoRepository(
    private val todoDao: TodoDao,
    private val timerSessionDao: TimerSessionDao,
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
            // Get local todos
            val localTodos = todoDao.getAllTodos().first()

            // Convert to API format
            val syncRequests = localTodos.map { todo ->
                TodoSyncRequest(
                    title = todo.title,
                    isCompleted = todo.isCompleted,
                    createdAt = todo.createdAt
                )
            }

            // Send to server and get response
            val response = apiService.syncTodos(syncRequests)

            if (response.isSuccessful) {
                // Sync successful - server now has our todos
                Log.d("Sync", "Successfully synced ${localTodos.size} todos")
            } else {
                Log.e("Sync", "Sync failed with code: ${response.code()}")
            }

        } catch (e: Exception) {
            Log.e("Sync", "Sync error: ${e.message}")
        }
    }
}
