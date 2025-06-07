package com.mobileSec.pomodoroapp.repository

import com.mobileSec.pomodoroapp.data.local.dao.TodoDao
import com.mobileSec.pomodoroapp.data.mapper.toDomainModel
import com.mobileSec.pomodoroapp.data.mapper.toEntity
import com.mobileSec.pomodoroapp.data.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)?.toDomainModel()
    }

    suspend fun saveTodo(todo: Todo) {
        todoDao.insertTodo(todo.toEntity())
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo.toEntity())
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo.toEntity())
    }

    suspend fun incrementPomodoro(todoId: Int) {
        todoDao.incrementPomodoro(todoId)
    }
}