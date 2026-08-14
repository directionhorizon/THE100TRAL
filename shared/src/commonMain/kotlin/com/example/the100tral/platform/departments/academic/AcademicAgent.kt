package com.example.the100tral.platform.departments.academic

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryEntry
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.ai.LLMService

/**
 * Agent Académique hérite directement de BaseAgent pour éviter le bug IR.
 */
class AcademicAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Agent Académique"
    override val authorityLevel: Int = 4
    override val domain: String = "ACADEMIC"

    override suspend fun dispatch(command: Command) {
        log("Lancement de la recherche scientifique pour : ${command.instruction}")

        // Consultation de la mémoire persistante
        val pastResearch = memoryStorage?.searchKnowledge(domain) ?: emptyList()
        
        // Utilisation du cycle de réflexion
        val resultMessage = thinkAndAct("Rédige un résumé scientifique de l'état de l'art pour : ${command.instruction}.")
        
        // Capitalisation
        memoryStorage?.saveKnowledge(MemoryEntry(domain, "Recherche sur: ${command.instruction} | Résultat: $resultMessage"))

        val report = createReport(
            command = command,
            status = ReportStatus.SUCCESS,
            message = resultMessage,
            data = mapOf(
                "state_of_the_art" to resultMessage
            )
        )

        log("Recherche terminée, transmission du mémoire.")
        commandChain.report(report)
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}



