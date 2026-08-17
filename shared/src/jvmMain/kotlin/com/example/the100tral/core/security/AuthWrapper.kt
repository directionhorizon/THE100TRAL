package com.example.the100tral.core.security

actual object AuthWrapper {
    private var simulatedEmail: String? = null

    actual fun getCurrentUserEmail(): String? {
        return simulatedEmail
    }

    actual fun signInSimulation(email: String) {
        simulatedEmail = email
    }
}
