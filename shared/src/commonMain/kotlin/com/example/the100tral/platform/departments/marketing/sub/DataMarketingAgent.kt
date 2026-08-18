package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class DataMarketingAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Agent Data & Geo Marketing"
    override val authorityLevel: Int = 4
    override val agentDomain: String = "DATA_MARKETING"

    override suspend fun dispatch(command: Command) {
        log("Analyse des flux de donnaƒÂ©es et segmentation gaƒÂ©ographique...")
        val thought = performAction("Analyse les segments de marchaƒÂ© et zones gaƒÂ©ographiques cibles pour : " + command.instruction)
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


