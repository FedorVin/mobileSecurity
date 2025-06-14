package com.mobilesec.pomodorotimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilesec.pomodorotimer.data.local.TodoEntity
import com.mobilesec.pomodorotimer.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val todos = repository.getAllTodos()

    private val _newTodoTitle = MutableStateFlow("")
    val newTodoTitle: StateFlow<String> = _newTodoTitle

    fun updateNewTodoTitle(title: String) {
        _newTodoTitle.value = title
    }

    fun addTodo() {
        if (_newTodoTitle.value.isNotBlank()) {
            viewModelScope.launch {
                repository.insertTodo(
                    TodoEntity(title = _newTodoTitle.value.trim())
                )
                _newTodoTitle.value = ""
            }
        }
    }

    fun toggleTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun syncTodos() {
        viewModelScope.launch {
            repository.syncTodos()
        }
    }
    fun fetchTodos() {
        viewModelScope.launch {
            repository.fetchTodosFromServer()
        }
    }

}
