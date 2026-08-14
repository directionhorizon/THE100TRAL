package com.example.the100tral.platform.departments.finance.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService

import com.example.the100tral.core.persistence.MemoryStorage

class AuditAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Audit"
    override val authorityLevel: Int = 4
    override val domain: String = "AUDIT"

    override suspend fun dispatch(command: Command) {
        log("Audit des ressources pour : ${command.instruction}")
        val thought = think("Estime les coûts et les risques pour : ${command.instruction}")
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




