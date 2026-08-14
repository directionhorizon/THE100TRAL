package com.example.the100tral.platform.management

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Arbitre de Crise (Niveau 2.5).
 * Intervient en cas de blocage systématique en AQ ou de panne systémique.
 */
class CrisisArbitrator(
    private val projectManager: ProjectManager,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Arbitre de Crise Interne"
    override val authorityLevel: Int = 2
    override val domain: String = "CRISIS_MANAGEMENT"

    private val failureCounts = mutableMapOf<String, Int>()

    override suspend fun dispatch(command: Command) {
        log("Analyse d'urgence pour la commande : ${command.id}")
        val resolution = think("Blocage détecté sur : ${command.instruction}. Quelle est la décision d'arbitrage ?")
        log("DÉCISION D'ARBITRAGE : $resolution")
    }

    override suspend fun report(result: Report) {
        // Surveille les échecs répétitifs
        if (result.status == ReportStatus.FAILURE) {
            val count = failureCounts.getOrDefault(result.commandId.toString(), 0) + 1
            failureCounts[result.commandId.toString()] = count
            
            if (count >= 3) {
                log("ALERTE CRITIQUE : 3 échecs consécutifs. Prise de contrôle de la mission.")
                // Logique pour simplifier l'ordre ou changer d'agent
            }
        }
    }
}
