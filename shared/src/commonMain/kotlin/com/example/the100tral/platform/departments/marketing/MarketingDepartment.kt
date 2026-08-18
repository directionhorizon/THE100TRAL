package com.example.the100tral.platform.departments.marketing

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Departement Marketing.
 * Orchestre les campagnes, les reseaux sociaux et la reputation digitale.
 */
class MarketingDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val agentIdentifier: String = "Pa´le Marketing"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "MARKETING"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Agent marketing enregistre : " + domain)
    }

    override suspend fun dispatch(command: Command) {
        log("Lancement de strategie marketing : " + command.instruction)
        subAgents.values.forEach { it.dispatch(command) }
    }

    override suspend fun report(result: Report) {
        log("Analyse du livrable marketing : " + result.commandId)
        commandChain.report(result)
    }
}


