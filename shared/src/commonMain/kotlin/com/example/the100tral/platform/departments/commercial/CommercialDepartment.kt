package com.example.the100tral.platform.departments.commercial

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class CommercialDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Pa´le Commercial"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "COMMERCIAL"

    override suspend fun dispatch(command: Command) {
        log("Negociation et partenariats : " + command.instruction)
        val response = performAction("a‰labore une strategie commerciale et de partenariat pour : " + command.instruction)
        commandChain.report(createReport(command, ReportStatus.SUCCESS, response))
    }
}


