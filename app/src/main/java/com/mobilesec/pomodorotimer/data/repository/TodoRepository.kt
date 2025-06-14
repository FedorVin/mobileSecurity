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

    suspend fun fetchTodosFromServer() {
        try {
            Log.d("Fetch", "Starting fetch from server...")

            // Get todos from server
            val response = apiService.getTodos()
            Log.d("Fetch", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                val serverTodos = response.body() ?: emptyList()
                Log.d("Fetch", "Server returned ${serverTodos.size} todos")

                // Clear all local todos FIRST
                todoDao.deleteAllTodos()
                Log.d("Fetch", "Cleared all local todos")

                // Insert server todos
                val localTodos = serverTodos.map { serverTodo ->
                    TodoEntity(
                        title = serverTodo.title,
                        isCompleted = serverTodo.isCompleted,
                        createdAt = serverTodo.createdAt
                    )
                }

                todoDao.insertAllTodos(localTodos)
                Log.d("Fetch", "Inserted ${localTodos.size} todos from server")

                Log.d("Fetch", "Successfully fetched ${serverTodos.size} todos from server")
            } else {
                Log.e("Fetch", "Fetch failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Fetch", "Fetch error: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun syncTodos() {
        try {
            val localTodos = todoDao.getAllTodos().first()
            val syncRequests = localTodos.map { todo ->
                TodoSyncRequest(
                    title = todo.title,
                    isCompleted = todo.isCompleted,
                    createdAt = todo.createdAt
                )
            }

            // Replace the entire todos array on the server
            val response = apiService.syncTodos(syncRequests)

            if (response.isSuccessful) {
                Log.d("Sync", "Successfully synced ${localTodos.size} todos")
            } else {
                Log.e("Sync", "Sync failed with code: ${response.code()}")
            }

        } catch (e: Exception) {
            Log.e("Sync", "Sync error: ${e.message}")
        }
    }


    //    suspend fun syncTodos() {
//        try {
//            // Get local todos
//            val localTodos = todoDao.getAllTodos().first()
//
//            // Convert to API format
//            val syncRequests = localTodos.map { todo ->
//                TodoSyncRequest(
//                    title = todo.title,
//                    isCompleted = todo.isCompleted,
//                    createdAt = todo.createdAt
//                )
//            }
//
//            // Send to server and get response
//            val response = apiService.syncTodos(syncRequests)
//
//            if (response.isSuccessful) {
//                // Sync successful - server now has our todos
//                Log.d("Sync", "Successfully synced ${localTodos.size} todos")
//            } else {
//                Log.e("Sync", "Sync failed with code: ${response.code()}")
//            }
//
//        } catch (e: Exception) {
//            Log.e("Sync", "Sync error: ${e.message}")
//        }
//    }

//    suspend fun fetchTodosFromServer() {
//        try {
//            // Get todos from server
//            val response = apiService.getTodos()
//
//            if (response.isSuccessful) {
//                val serverTodos = response.body() ?: emptyList()
//
//                todoDao.deleteAllTodos() // You'll need to add this method
//
//                // Clear all local todos
//                val localTodos = todoDao.getAllTodos().first()
//                localTodos.forEach { todo ->
//                    todoDao.deleteTodo(todo)
//                }
//
//                // Insert server todos
//                serverTodos.forEach { serverTodo ->
//                    val localTodo = TodoEntity(
//                        title = serverTodo.title,
//                        isCompleted = serverTodo.isCompleted,
//                        createdAt = serverTodo.createdAt
//                    )
//                    todoDao.insertTodo(localTodo)
//                }
//
//                Log.d("Fetch", "Successfully fetched ${serverTodos.size} todos from server")
//            } else {
//                Log.e("Fetch", "Fetch failed with code: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Log.e("Fetch", "Fetch error: ${e.message}")
//        }
//    }

//    suspend fun syncTodos() {
//        try {
//            // STEP 1: Send local todos to server
//            val localTodos = todoDao.getAllTodos().first()
//            val syncRequests = localTodos.map { todo ->
//                TodoSyncRequest(
//                    title = todo.title,
//                    isCompleted = todo.isCompleted,
//                    createdAt = todo.createdAt
//                )
//            }
//
//
//            // Send to server
//            val uploadResponse = apiService.syncTodos(syncRequests)
//
//            if (uploadResponse.isSuccessful) {
//                Log.d("Sync", "Successfully uploaded ${localTodos.size} todos")
//
//                // STEP 2: Get updated todos from server (this replaces local data)
//                val downloadResponse = apiService.getTodos()
//
//                if (downloadResponse.isSuccessful) {
//                    val serverTodos = downloadResponse.body() ?: emptyList()
//
//                    // STEP 3: Clear local database and insert server data
//                    // Delete all local todos
//                    localTodos.forEach { todo ->
//                        todoDao.deleteTodo(todo)
//                    }
//
//                    // Insert server todos
//                    serverTodos.forEach { serverTodo ->
//                        val localTodo = TodoEntity(
//                            title = serverTodo.title,
//                            isCompleted = serverTodo.isCompleted,
//                            createdAt = serverTodo.createdAt
//                        )
//                        todoDao.insertTodo(localTodo)
//                    }
//
//                    Log.d(
//                        "Sync",
//                        "Successfully synced: downloaded ${serverTodos.size} todos from server"
//                    )
//                } else {
//                    Log.e("Sync", "Download failed with code: ${downloadResponse.code()}")
//                }
//            } else {
//                Log.e("Sync", "Upload failed with code: ${uploadResponse.code()}")
//            }
//
//        } catch (e: Exception) {
//            Log.e("Sync", "Sync error: ${e.message}")
//        }
//    }
}