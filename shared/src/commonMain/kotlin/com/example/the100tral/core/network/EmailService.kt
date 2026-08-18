package com.example.the100tral.core.network

import com.example.the100tral.core.monitor.ThoughtMonitor

class EmailService {
    data class Email(val from: String, val to: String, val subject: String, val body: String)
    suspend fun sendEmail(to: String, subject: String, body: String): Boolean {
        ThoughtMonitor.updateThought("EmailSystem", "NETWORK", "Email envoyÃ© Ã  " + to)
        return true
    }
}

