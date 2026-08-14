package com.example.the100tral.platform.departments.sales.sub

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent Relation Client (Niveau 4).
 * Gère les retours clients, les plaintes et la satisfaction.
 */
class CustomerSuccessAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Customer Success Agent"
    override val authorityLevel: Int = 4
    override val domain: String = "CUSTOMER_RELATIONS"

    override suspend fun dispatch(command: Command) {
        log("Analyse des retours clients : ${command.instruction}")
        
        val analysis = think("""
            Analyse la satisfaction client basée sur : ${command.instruction}.
            Si un bug majeur est détecté, signale-le explicitement.
        """.trimIndent())
        
        log("Analyse satisfaction terminée.")
        report(createReport(command, ReportStatus.SUCCESS, analysis))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




