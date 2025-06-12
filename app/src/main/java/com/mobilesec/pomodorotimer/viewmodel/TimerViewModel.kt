package com.mobilesec.pomodorotimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilesec.pomodorotimer.data.local.TimerSessionEntity
import com.mobilesec.pomodorotimer.data.repository.TodoRepository
import com.mobilesec.pomodorotimer.utils.SecurityBypass
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _timeRemaining = MutableStateFlow(25 * 60) // 25 minutes
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _isWorkSession = MutableStateFlow(true)
    val isWorkSession: StateFlow<Boolean> = _isWorkSession

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    init {
        checkPremiumStatus()
    }

    fun startTimer() {
        // SECURITY BYPASS POINT: Can be bypassed with Frida

        _isRunning.value = true
        viewModelScope.launch {
            while (_timeRemaining.value > 0 && _isRunning.value) {
                delay(1000)
                _timeRemaining.value--
            }
            if (_timeRemaining.value == 0) {
                onTimerComplete()
            }
        }
    }

    // SECURITY BYPASS POINT: Can be bypassed with Frida
    fun canShowSeconds(): Boolean {
        return SecurityBypass.canShowSeconds() || _isPremium.value
    }

    fun pauseTimer() {
        _isRunning.value = false
    }

    fun resetTimer() {
        _isRunning.value = false
        _timeRemaining.value = if (_isWorkSession.value) 25 * 60 else 5 * 60
    }

    private fun onTimerComplete() {
        viewModelScope.launch {
            val session = TimerSessionEntity(
                duration = if (_isWorkSession.value) 25 else 5,
                type = if (_isWorkSession.value) "work" else "break"
            )
            repository.insertTimerSession(session)

            _isWorkSession.value = !_isWorkSession.value
            resetTimer()
        }
    }

    private fun checkPremiumStatus() {
        viewModelScope.launch {
            val credentials = repository.getCredentials()
            _isPremium.value = credentials?.isPremium ?: false
        }
    }

    private val _promoCode = MutableStateFlow("")
    val promoCode: StateFlow<String> = _promoCode

    fun updatePromoCode(code: String) {
        _promoCode.value = code
    }

    // SECURITY BYPASS POINT: Can be bypassed with Frida
    fun redeemPromoCode() {
        if (SecurityBypass.validatePromoCode(_promoCode.value)) {
            viewModelScope.launch {
                repository.updatePremiumStatus(true)
                _isPremium.value = true
                _promoCode.value = ""
            }
        }
    }


    // Function that can be bypassed with Frida
    fun upgradeToPremium() {
        if (SecurityBypass.canUpgradeToPremium()) {
            viewModelScope.launch {
                repository.updatePremiumStatus(true)
                _isPremium.value = true
            }
        }
    }
}
