package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService

import com.example.the100tral.core.persistence.MemoryStorage

class DataMarketingAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Data & Geo Marketing"
    override val authorityLevel: Int = 4
    override val domain: String = "DATA_MARKETING"

    override suspend fun dispatch(command: Command) {
        log("Analyse des flux de données et segmentation géographique...")
        val thought = think("Analyse les segments de marché et zones géographiques cibles pour : ${command.instruction}")
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




