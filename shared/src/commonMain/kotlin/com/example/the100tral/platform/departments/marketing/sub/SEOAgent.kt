package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class SEOAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Agent SEO IA"
    override val authorityLevel: Int = 4
    override val agentDomain: String = "SEO"

    override suspend fun dispatch(command: Command) {
        log("Optimisation saƒÂ©mantique pour le SEO...")
        val thought = performAction("GaƒÂ©naƒÂ¨re un strataƒÂ©gie SEO et des mots-claƒÂ©s pour : " + command.instruction)
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


