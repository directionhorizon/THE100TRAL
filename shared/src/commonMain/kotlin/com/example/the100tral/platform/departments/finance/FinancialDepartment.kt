package com.example.the100tral.platform.departments.finance

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Pôle Finance - VERSION OPÉRATIONNELLE.
 * Analyse les coûts et la rentabilité des évolutions structurelles.
 */
class FinancialDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val agentIdentifier: String = "Pôle Finance"
    override val authorityLevel: Int = 3
    override val agentDomain: String = "FINANCE"

    override suspend fun dispatch(command: Command) {
        log("Analyse financière pour : " + command.instruction)
        
        val budgetAnalysis = performAction("ANALYSE FINANCIÈRE : Calcule le coût estimé et le ROI pour la création du rôle ou de la compétence : " + command.instruction)
        
        val report = createReport(command, ReportStatus.SUCCESS, "RAPPORT ROI & BUDGET :\n" + budgetAnalysis)
        commandChain.report(report)
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}
