package com.example.the100tral.platform.departments.finance.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class AuditAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Agent d'Audit"
    override val authorityLevel: Int = 4
    override val domain: String = "AUDIT"

    override suspend fun dispatch(command: Command) {
        log("Vérification de la conformité et des risques...")
        val audit = thinkAndAct("Réalise un audit sur : ${command.instruction}. Utilise FINANCIAL_ANALYZER_TOOL.")
        report(createReport(command, ReportStatus.SUCCESS, audit))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}

