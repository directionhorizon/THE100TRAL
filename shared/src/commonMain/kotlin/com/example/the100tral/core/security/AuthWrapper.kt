package com.example.the100tral.core.security

expect object AuthWrapper {
    fun getCurrentUserEmail(): String?
    fun signInSimulation(email: String)
}

