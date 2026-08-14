package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.network.EmailService
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult

/**
 * Outil pour l'envoi immédiat d'email.
 */
class SendEmailTool(private val emailService: EmailService) : ITool {
    override val toolName: String = "SendEmail"
    override val description: String = "Envoie un email immédiatement. Paramètres: to, subject, body"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val to = params["to"]?.toString() ?: return ToolResult(success = false, output = "Destinataire 'to' manquant.")
        val subject = params["subject"]?.toString() ?: ""
        val body = params["body"]?.toString() ?: ""

        val success = emailService.sendEmail(to, subject, body)
        return ToolResult(success, if (success) "Email envoyé à $to" else "Échec de l'envoi")
    }
}
