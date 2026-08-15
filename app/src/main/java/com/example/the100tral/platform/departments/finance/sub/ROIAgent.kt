package com.example.the100tral.platform.departments.finance.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class ROIAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Agent ROI"
    override val authorityLevel: Int = 4
    override val domain: String = "ROI_ANALYSIS"

    override suspend fun dispatch(command: Command) {
        log("Calcul de la rentabilité et performance...")
        val roi = thinkAndAct("Calcule le ROI prévisionnel pour : ${command.instruction}.")
        report(createReport(command, ReportStatus.SUCCESS, roi))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


