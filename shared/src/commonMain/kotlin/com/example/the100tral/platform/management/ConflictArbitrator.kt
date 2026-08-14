package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
// Removed java.util.UUID

/**
 * Arbitre les conflits entre les départements spécialisés.
 */
class ConflictArbitrator(private val llmService: LLMService?) {

    /**
     * Analyse un ensemble de rapports et produit une décision cohérente.
     */
    suspend fun arbitrate(reports: List<Report>): Report {
        if (reports.isEmpty()) return Report("INTERNAL_ERR", ReportStatus.FAILURE, message = "Aucun rapport à arbitrer")

        val context = reports.joinToString("\n") { 
            "Département: ${it.status} | Message: ${it.message}"
        }

        val prompt = """
            Tu es l'arbitre suprême de la plateforme THE 100TRAL. 
            Voici les rapports des départements :
            $context
            
            Analyse les contradictions (ex: budget vs ambition marketing) et prends une décision finale cohérente.
            Réponds par un résumé de la décision finale.
        """.trimIndent()

        val decision = llmService?.think(prompt) ?: "Arbitrage automatique : Tous les rapports sont acceptés. [Mock]"

        return Report(
            commandId = reports.first().commandId,
            status = ReportStatus.SUCCESS,
            message = decision
        )
    }
}
