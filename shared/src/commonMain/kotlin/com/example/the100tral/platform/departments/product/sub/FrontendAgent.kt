package com.example.the100tral.platform.departments.product.sub

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class FrontendAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Front-end"
    override val authorityLevel: Int = 4
    override val domain: String = "FRONTEND"

    override suspend fun dispatch(command: Command) {
        log("Création des interfaces utilisateur...")
        val thought = think("Conçois une interface moderne pour : ${command.instruction}")
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




