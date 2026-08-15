package com.example.the100tral.platform.departments.sales

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Ventes et Relation Client (Niveau 3).
 */
class SalesDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Département Sales & Customer Success"
    override val authorityLevel: Int = 3
    override val domain: String = "SALES_CS"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Sous-agent Sales Niveau 4 enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Analyse de la demande commerciale...")
        // Délégation automatique aux sous-agents
        subAgents.values.forEach { agent ->
            agent.dispatch(command)
        }
    }

    override suspend fun report(result: Report) {
        log("Rapport de vente reçu. Transmission au Chef de Projet.")
        commandChain.report(result)
    }
}






