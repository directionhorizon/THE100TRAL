package com.example.the100tral.platform.departments.sales.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class LeadHunter(
    private val parent: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Chasseur de Leads"
    override val authorityLevel: Int = 4
    override val agentDomain: String = "LEAD_GEN"

    override suspend fun dispatch(command: Command) {
        log("Identification de cibles commerciales pour : " + command.instruction)
        val response = performAction("Trouve des segments de clients potentiels pour : " + command.instruction)
        parent.report(createReport(command, ReportStatus.SUCCESS, response))
    }
}


