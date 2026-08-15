package com.example.the100tral.core.security

import com.google.firebase.auth.FirebaseAuth

object AuthWrapper {
    private var simulatedEmail: String? = null

    fun getCurrentUserEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email ?: simulatedEmail
    }

    fun signInSimulation(email: String) {
        simulatedEmail = email
    }
}


