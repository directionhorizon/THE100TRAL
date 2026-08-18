package com.example.the100tral.platform.departments.sales

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class SalesDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Pa´le Ventes"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "SALES"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
    }

    override suspend fun dispatch(command: Command) {
        log("Conversion et croissance : " + command.instruction)
        subAgents.values.forEach { it.dispatch(command) }
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


