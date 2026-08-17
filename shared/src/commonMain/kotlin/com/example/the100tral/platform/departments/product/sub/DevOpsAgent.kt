package com.example.the100tral.platform.departments.product.sub

import com.example.the100tral.core.contract.*
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
        log("Mise en place de l'infrastructure et build...")
        val buildResult = thinkAndAct("""
            Gère l'automatisation pour : ${command.instruction}
            1. Utilise BUILD_TOOL pour vérifier l'intégrité du projet.
            2. Configure les secrets pour Firebase.
        """.trimIndent())
        
        report(createReport(command, ReportStatus.SUCCESS, "Infrastructure validée : $buildResult"))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}

