package com.example.the100tral.platform.departments.finance

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay
import kotlin.random.Random

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Financier.
 * Niveau 3 : Orchestrateur financier pour le Niveau 4.
 */
class FinancialDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Département Financier"
    override val authorityLevel: Int = 3
    override val domain: String = "FINANCE"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Sous-agent financier Niveau 4 enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Audit financier global lancé. Délégation aux analystes...")
        subAgents.values.forEach { agent ->
            agent.dispatch(command)
        }
    }

    override suspend fun report(result: Report) {
        log("Rapport financier reçu. Transmission au Chef de Projet.")
        commandChain.report(result)
    }
}




