package com.example.the100tral.platform.departments.marketing

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Marketing - VERSION RESTAURÉE.
 * Orchestre les campagnes, les réseaux sociaux et la réputation digitale.
 */
class MarketingDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Pôle Marketing"
    override val authorityLevel: Int = 3
    override val domain: String = "MARKETING"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Agent marketing enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Lancement de stratégie marketing : ${command.instruction}")
        
        // Délégation automatique aux sous-agents (Social Media, Reputation, etc.)
        subAgents.values.forEach { it.dispatch(command) }
    }

    override suspend fun report(result: Report) {
        log("Analyse du livrable marketing : ${result.commandId}")
        commandChain.report(result)
    }
}


