package com.example.the100tral.platform.departments.finance

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class FinancialDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Pôle Finance"
    override val authorityLevel: Int = 3
    override val domain: String = "FINANCE"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
    }

    override suspend fun dispatch(command: Command) {
        log("Analyse financière lancée pour : ${command.instruction}")
        subAgents.values.forEach { it.dispatch(command) }
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


