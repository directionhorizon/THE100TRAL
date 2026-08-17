package com.example.the100tral.platform.departments.sales.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class LeadHunter(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Agent Lead Hunter"
    override val authorityLevel: Int = 4
    override val domain: String = "LEADS"

    override suspend fun dispatch(command: Command) {
        log("Chasse aux opportunités commerciales...")
        val leads = thinkAndAct("Identifie 5 leads potentiels pour : ${command.instruction}. Utilise TAVILY_SEARCH.")
        report(createReport(command, ReportStatus.SUCCESS, leads))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}

