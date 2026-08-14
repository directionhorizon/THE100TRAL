package com.example.the100tral.platform.management

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.BaseAgent
import com.example.the100tral.core.contract.Command
import com.example.the100tral.core.contract.Report
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent Secrétaire de Direction (Niveau 2 bis).
 * Gère l'agenda du Super-Orchestrateur et coordonne les rappels avec le Chef de Projet.
 */
class ExecutiveAssistant(
    private val projectManager: ProjectManager,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Secrétaire de Direction"
    override val authorityLevel: Int = 2
    override val domain: String = "EXECUTIVE_SUPPORT"

    override suspend fun dispatch(command: Command) {
        log("Réception d'une demande de support : ${command.instruction}")
        
        // La secrétaire peut décider de créer un rappel OU de demander une info au Chef de Projet
        val response = think("Tâche : ${command.instruction}. Dois-je créer un rappel ou interroger le Chef de Projet ?")
        
        if (response.contains("rappel", ignoreCase = true)) {
            // Logique de création de rappel automatique via outil...
            log("Décision : Planification d'un rappel.")
        }
    }

    override suspend fun report(result: Report) {
        // La secrétaire peut recevoir des rapports pour les classer
        log("Classement du rapport : ${result.message}")
    }
}
