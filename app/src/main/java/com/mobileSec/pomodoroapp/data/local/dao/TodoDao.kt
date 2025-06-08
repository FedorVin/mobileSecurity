package com.mobileSec.pomodoroapp.data.local.dao

import androidx.room.*
import com.mobileSec.pomodoroapp.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY id ASC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateTodo(todo: TodoEntity): Int

    @Delete
    suspend fun deleteTodo(todo: TodoEntity): Int

    @Query("UPDATE todos SET pomodoros_completed = pomodoros_completed + 1 WHERE id = :todoId")
    suspend fun incrementPomodoro(todoId: Int): Int
}