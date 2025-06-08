package com.mobilesec.pomodorotimer.utils

object SecurityBypass {
    // VULNERABILITY: Can be bypassed with Frida
    fun canStartTimer(): Boolean {
        return checkPremiumStatus()
    }

    // VULNERABILITY: Can be bypassed with Frida
    fun canUpgradeToPremium(): Boolean {
        return false // Always returns false, but can be bypassed
    }

    // VULNERABILITY: Weak security check
    private fun checkPremiumStatus(): Boolean {
        val secretKey = "hardcoded_secret_123" // VULNERABILITY: Hardcoded key
        return secretKey == "premium_unlock_key" // Always false
    }

    // VULNERABILITY: Plain text password validation
    fun validateCredentials(username: String, password: String): Boolean {
        // VULNERABILITY: Hardcoded credentials
        return username == "admin" && password == "password123"
    }

    // Function that can be hooked with Frida to bypass restrictions
    fun isPremiumFeatureEnabled(): Boolean {
        return false // Can be changed to true with Frida
    }
}
