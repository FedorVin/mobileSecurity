package com.mobileSec.pomodoroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobileSec.pomodoroapp.malware.MalwareService
import com.mobileSec.pomodoroapp.repository.TimerRepository
import com.mobileSec.pomodoroapp.repository.TodoRepository

class MainViewModelFactory(
    private val todoRepository: TodoRepository,
    private val timerRepository: TimerRepository,
    private val malwareService: MalwareService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(todoRepository, timerRepository, malwareService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}