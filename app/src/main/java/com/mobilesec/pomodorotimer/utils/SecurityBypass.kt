package com.mobilesec.pomodorotimer.utils

object SecurityBypass {
    // VULNERABILITY: Hardcoded promo codes (acts as plain text credentials)
    fun validatePromoCode(code: String): Boolean {
        val validCodes = listOf("PREMIUM2025", "FREETRIAL", "UNLOCK50")
        return validCodes.contains(code.uppercase())
    }

    // VULNERABILITY: Can be bypassed with Frida to show seconds
    fun canShowSeconds(): Boolean {
        return false // Returns false by default, can be bypassed
    }
}


//object SecurityBypass {
//    // VULNERABILITY: Hardcoded promo codes (acts as plain text credentials)
//    fun validatePromoCode(code: String): Boolean {
//        val validCodes = listOf("PREMIUM2025", "FREETRIAL", "UNLOCK50")
//        return validCodes.contains(code.uppercase())
//    }
//
//    // VULNERABILITY: Can be bypassed with Frida to show seconds
//    fun canShowSeconds(): Boolean {
//        return false // Returns false by default, can be bypassed
//    }
//}
