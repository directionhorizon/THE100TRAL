package com.example.the100tral.core.security

import com.google.firebase.auth.FirebaseAuth

actual object AuthWrapper {
    private var simulatedEmail: String? = null

    actual fun getCurrentUserEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email ?: simulatedEmail
    }

    actual fun signInSimulation(email: String) {
        simulatedEmail = email
    }
}
