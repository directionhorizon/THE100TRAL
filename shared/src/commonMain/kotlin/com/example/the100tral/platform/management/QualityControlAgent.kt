package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * AGENT QUALITa‰.
 * Collaborateur direct du PM pour valider la faisabilite des evolutions de ra´le.
 */
class QualityControlAgent(
    private val pm: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val agentIdentifier: String = "Agent Qualite"
    override val agentDomain: String = "QUALITY_CONTROL"
    override val authorityLevel: Int = 3

    override suspend fun dispatch(command: Command) {
        log("Identification du besoin metier pour : " + command.instruction)
        
        val feasibility = performAction("ANALYSE DE FAISABILITa‰ : Identifie le besoin metier exact a  creer ou modifier. Est-ce realisable dans la structure actuelle ? Analyse les risques.")
        
        val report = createReport(command, ReportStatus.SUCCESS, "ANALYSE QUALITa‰ & FAISABILITa‰ :\n" + feasibility)
        pm.report(report)
    }

    override suspend fun report(result: Report) {
        pm.report(result)
    }
}


