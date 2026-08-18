package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class ConflictArbitrator(
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Arbitre de Conflits"
    override val authorityLevel: Int = 2
    override val agentDomain: String = "CONFLICT_RESOLUTION"

    override suspend fun dispatch(command: Command) {
        log("Mediation en cours : " + command.instruction)
        val response = performAction("Resous le conflit suivant entre agents : " + command.instruction)
        report(createReport(command, ReportStatus.SUCCESS, response))
    }
}


