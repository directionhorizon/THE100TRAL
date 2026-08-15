package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent de Communication de Crise (Niveau 4).
 * Protège l'image de marque et gère les relations publiques digitales.
 */
class DigitalReputationAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Agent Réputation & Com de Crise"
    override val authorityLevel: Int = 4
    override val domain: String = "DIGITAL_PR"

    override suspend fun dispatch(command: Command) {
        log("Analyse de l'image de marque pour : ${command.instruction}")
        
        val strategy = thinkAndAct("""
            Contexte : ${command.instruction}
            Tâche : Analyse les risques de réputation en utilisant les outils d'écoute sociale et prépare une réponse officielle protectrice.
        """.trimIndent())
        
        log("Stratégie de défense générée : $strategy")
        report(createReport(command, ReportStatus.SUCCESS, strategy))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}






