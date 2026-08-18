package com.example.the100tral.platform.departments.academic

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class AcademicAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Pa´le Academique"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "ACADEMIC_RESEARCH"

    override suspend fun dispatch(command: Command) {
        log("Recherche theorique et rigueur : " + command.instruction)
        val response = performAction("Analyse d'un point de vue academique et source : " + command.instruction)
        commandChain.report(createReport(command, ReportStatus.SUCCESS, response))
    }
}


