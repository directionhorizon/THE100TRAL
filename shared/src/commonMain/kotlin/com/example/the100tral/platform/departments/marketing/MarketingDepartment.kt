package com.example.the100tral.platform.departments.marketing

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Marketing Multi-canal.
 * Niveau 3 : Orchestrateur pour le Niveau 4.
 */
class MarketingDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Département Marketing Multi-canal"
    override val authorityLevel: Int = 3
    override val domain: String = "MARKETING"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Sous-agent Niveau 4 enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Analyse de la demande marketing. Délégation aux experts...")
        subAgents.values.forEach { agent ->
            agent.dispatch(command)
        }
    }

    override suspend fun report(result: Report) {
        log("Rapport reçu d'un expert marketing. Transmission au Chef de Projet.")
        commandChain.report(result)
    }
}




