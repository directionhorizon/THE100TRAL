package com.example.the100tral.platform.management

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent AQ (Niveau 2.5).
 * Filtre, vérifie et adapte les rapports opérationnels pour la direction.
 */
class QualityControlAgent(
    private val projectManager: ProjectManager,
    private val executiveAssistant: ExecutiveAssistant?,
    private val superOrchestrator: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Contrôleur Qualité & Synthèse"
    override val authorityLevel: Int = 4 // Niveau opérationnel (Agent)
    override val domain: String = "QUALITY_ASSURANCE"

    override suspend fun dispatch(command: Command) {
        // L'AQ ne reçoit pas d'ordres directs de mission, il intercepte les retours.
        log("AQ prêt pour la vérification des flux montants.")
    }

    override suspend fun report(result: Report) {
        log("Interception du rapport provenant de : ${result.commandId}")
        
        val evaluation = think("""
            Analyse ce rapport : ${result.message}
            Données brutes : ${result.data}
            
            1. Le travail est-il conforme ? (OUI/NON)
            2. Si OUI, prépare 3 résumés : 
               - TECHNIQUE (pour Chef Projet)
               - EXÉCUTIF (pour Secrétaire)
               - STRATÉGIQUE (pour Super-Orchestrateur)
        """.trimIndent())

        if (evaluation.contains("NON", ignoreCase = true)) {
            log("QUALITÉ INSUFFISANTE : Rejet du travail.")
            // L'AQ signale l'échec pour le monitoring de crise
            projectManager.report(result.copy(status = ReportStatus.FAILURE, message = "Échec Qualité : $evaluation"))
        } else {
            log("QUALITÉ VALIDÉE : Distribution des synthèses adaptées.")
            
            // On distribue l'info formatée à chaque niveau
            projectManager.report(result.copy(message = "RAPPORT TECHNIQUE : $evaluation"))
            executiveAssistant?.report(result.copy(message = "NOTE D'AGENDA : Travail terminé avec succès."))
            superOrchestrator.report(result.copy(message = "SYNTHÈSE STRATÉGIQUE : $evaluation"))
        }
    }
}


