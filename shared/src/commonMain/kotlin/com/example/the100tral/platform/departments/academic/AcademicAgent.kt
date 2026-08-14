package com.example.the100tral.platform.departments.academic

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryEntry
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.ai.LLMService

/**
 * Agent Académique - VERSION RESTAURÉE (Vision 2.0).
 * Responsable de la veille scientifique réelle via Tavily et ArXiv.
 */
class AcademicAgent(
    private val commandChain: BaseAgent,
    memoryStorage: MemoryStorage,
    llmService: LLMService? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Agent Académique"
    override val authorityLevel: Int = 4
    override val domain: String = "ACADEMIC"

    override suspend fun dispatch(command: Command) {
        log("Lancement de la recherche scientifique pour : ${command.instruction}")

        // 1. Consultation de la mémoire IA
        val pastResearch = memoryStorage?.searchKnowledge(domain) ?: emptyList()
        
        // 2. Action réelle : Utilisation de Tavily et ArXiv via le cycle de réflexion
        // On demande explicitement à l'IA d'utiliser l'outil TAVILY_SEARCH
        val researchTask = """
            Effectue une recherche approfondie sur : ${command.instruction}
            Utilise l'outil TAVILY_SEARCH pour obtenir des faits réels et récents.
            Utilise l'outil ARXIV_RESEARCH pour les papiers scientifiques si nécessaire.
            Produis un résumé de haut niveau.
        """.trimIndent()
        
        val resultMessage = thinkAndAct(researchTask)
        
        // 3. Capitalisation dans la mémoire partagée
        memoryStorage?.saveKnowledge(MemoryEntry(domain, "État de l'art sur: ${command.instruction} | Résumé: $resultMessage", source = "ACADEMIC_AGENT"))

        // 4. Rapport ordonné
        val report = createReport(
            command = command,
            status = ReportStatus.SUCCESS,
            message = resultMessage,
            data = mapOf(
                "research_outcome" to resultMessage,
                "sources_checked" to "Web + ArXiv"
            )
        )

        log("Recherche terminée, transmission au supérieur.")
        commandChain.report(report)
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}
