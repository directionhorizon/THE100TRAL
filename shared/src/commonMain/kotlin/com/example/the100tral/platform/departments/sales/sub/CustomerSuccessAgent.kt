package com.example.the100tral.platform.departments.sales.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class CustomerSuccessAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Agent Customer Success"
    override val authorityLevel: Int = 4
    override val domain: String = "CUSTOMER_CARE"

    override suspend fun dispatch(command: Command) {
        log("Analyse de la satisfaction et support client...")
        val plan = thinkAndAct("Prépare un plan de réponse client pour : ${command.instruction}. Utilise SEND_EMAIL_TOOL.")
        report(createReport(command, ReportStatus.SUCCESS, plan))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}

