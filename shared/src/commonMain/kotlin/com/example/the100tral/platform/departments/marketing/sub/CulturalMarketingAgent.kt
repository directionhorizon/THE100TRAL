package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService

import com.example.the100tral.core.persistence.MemoryStorage

class CulturalMarketingAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Marketing Culturel"
    override val authorityLevel: Int = 4
    override val domain: String = "CULTURAL_MARKETING"

    override suspend fun dispatch(command: Command) {
        log("Adaptation culturelle du message marketing...")
        val thought = think("Adapte le message marketing aux codes culturels spécifiques pour : ${command.instruction}")
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




