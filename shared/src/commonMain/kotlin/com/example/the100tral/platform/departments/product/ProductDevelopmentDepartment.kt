package com.example.the100tral.platform.departments.product

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Pôle Produit (DEV) - VERSION OPÉRATIONNELLE.
 * S'auto-développe en fonction des besoins techniques détectés.
 */
class ProductDevelopmentDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val agentIdentifier: String = "Pôle Produit (DEV)"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "PRODUCT_DEV"

    override suspend fun dispatch(command: Command) {
        log("Analyse des besoins techniques pour : " + command.instruction)
        
        val techAnalysis = performAction("ANALYSE DEV : En tant que lead dev, définis les nouvelles compétences ou agents techniques nécessaires pour réaliser : " + command.instruction)
        
        val report = createReport(command, ReportStatus.SUCCESS, "PROPOSITION TECHNIQUE :\n" + techAnalysis)
        commandChain.report(report)
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}
