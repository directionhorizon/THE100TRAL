package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService

import com.example.the100tral.core.persistence.MemoryStorage

class CrawlerAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Crawler & Scrapping"
    override val authorityLevel: Int = 4
    override val domain: String = "CRAWLER"

    override suspend fun dispatch(command: Command) {
        log("Exploration et scrapping (Deep Web Légal)...")
        val thought = think("Simule une extraction de données et une veille concurrentielle pour : ${command.instruction}")
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}






