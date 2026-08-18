package com.example.the100tral.platform.departments.visual

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class VisualStudioDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Visual Studio"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "VISUAL_DESIGN"

    override suspend fun dispatch(command: Command) {
        log("Conception graphique et identite : " + command.instruction)
        val response = performAction("Cree un concept visuel et une charte pour : " + command.instruction)
        commandChain.report(createReport(command, ReportStatus.SUCCESS, response))
    }
}


