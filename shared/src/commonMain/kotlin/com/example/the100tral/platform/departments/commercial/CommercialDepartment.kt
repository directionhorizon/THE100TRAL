package com.example.the100tral.platform.departments.commercial

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Pôle Commercial (Niveau 3).
 * Regroupe les Sales, la prospection et la relation client.
 */
class CommercialDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Pôle Commercial"
    override val authorityLevel: Int = 3
    override val domain: String = "COMMERCIAL"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Agent commercial enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Traitement d'une mission commerciale : ${command.instruction}")
        subAgents.values.forEach { it.dispatch(command) }
    }

    override suspend fun report(result: Report) {
        log("Rapport commercial reçu (${result.status}). Remontée au Manager.")
        commandChain.report(result)
    }
}




