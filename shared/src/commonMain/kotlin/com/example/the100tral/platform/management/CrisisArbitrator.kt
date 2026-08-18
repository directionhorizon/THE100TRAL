package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class CrisisArbitrator(
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Arbitre de Crise"
    override val authorityLevel: Int = 2
    override val agentDomain: String = "CRISIS_MANAGEMENT"

    override suspend fun dispatch(command: Command) {
        log("Analyse d'urgence : " + command.instruction)
        val response = performAction("Propose une solution de sortie de crise pour : " + command.instruction)
        report(createReport(command, ReportStatus.SUCCESS, response))
    }
}


