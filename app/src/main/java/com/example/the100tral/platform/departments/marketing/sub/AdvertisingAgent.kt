package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService

import com.example.the100tral.core.persistence.MemoryStorage

class AdvertisingAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Publicité"
    override val authorityLevel: Int = 4
    override val domain: String = "ADVERTISING"

    override suspend fun dispatch(command: Command) {
        log("Création de la campagne publicitaire pour : ${command.instruction}")
        val thought = think("Rédige un message publicitaire percutant pour : ${command.instruction}")
        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}






