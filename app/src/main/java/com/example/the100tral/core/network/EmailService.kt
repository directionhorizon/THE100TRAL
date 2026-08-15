package com.example.the100tral.core.network

import com.example.the100tral.core.monitor.ThoughtMonitor

/**
 * Service de gestion des Emails.
 * En mode simulation par défaut, prêt pour SMTP/IMAP.
 */
class EmailService {

    data class Email(
        val from: String,
        val to: String,
        val subject: String,
        val body: String,
        val timestamp: Long = 0L // Utiliser 0 par défaut ou une abstraction pour KMP
    )

    private val receivedEmails = mutableListOf<Email>()

    /**
     * Envoie un email (Simulé pour l'instant).
     */
    suspend fun sendEmail(to: String, subject: String, body: String): Boolean {
        // Simulation d'envoi réseau
        println("EmailService: Envoi d'email à $to: $subject")
        ThoughtMonitor.publish("EmailSystem", "NETWORK", "Email envoyé à $to | Sujet: $subject")
        return true
    }

    /**
     * Récupère les derniers emails reçus.
     */
    suspend fun fetchInbox(): List<Email> {
        // Simulation de réception
        return receivedEmails.toList()
    }

    /**
     * Ajoute un email à la boîte de réception (pour le test/démo).
     */
    fun simulateReceivedEmail(from: String, subject: String, body: String) {
        receivedEmails.add(Email(from, "me@the100tral.com", subject, body))
    }
}


