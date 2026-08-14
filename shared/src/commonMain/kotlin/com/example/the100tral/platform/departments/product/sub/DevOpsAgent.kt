package com.example.the100tral.platform.departments.product.sub

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class DevOpsAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent DevOps"
    override val authorityLevel: Int = 4
    override val domain: String = "DEVOPS"

    override suspend fun dispatch(command: Command) {
        log("Configuration de l'infrastructure...")
        val thought = think("Définis le pipeline CI/CD pour : ${command.instruction}")
        
        // Utilisation réelle de l'outil de build
        useTool("BUILD_LAUNCHER", mapOf("project" to command.instruction))

        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




