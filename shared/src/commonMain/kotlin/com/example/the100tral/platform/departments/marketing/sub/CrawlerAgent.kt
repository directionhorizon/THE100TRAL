package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class CrawlerAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Agent Crawler & Scrapping"
    override val authorityLevel: Int = 4
    override val agentDomain: String = "CRAWLER"

    override suspend fun dispatch(command: Command) {
        log("Exploration et scrapping (Deep Web LaƒÂ©gal)...")
        val thought = performAction("Simule une extraction de donnaƒÂ©es et une veille concurrentielle pour : " + command.instruction)
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


