package com.example.the100tral.platform.departments.sales.sub

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent Chasseur de Leads (Niveau 4).
 * Identifie des opportunités commerciales et des prospects.
 */
class LeadHunter(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Lead Hunter"
    override val authorityLevel: Int = 4
    override val domain: String = "LEAD_GENERATION"

    override suspend fun dispatch(command: Command) {
        log("Recherche de nouveaux prospects pour : ${command.instruction}")
        
        val prospects = think("""
            Objectif : Trouver des clients potentiels pour ${command.instruction}.
            Utilise les connaissances du marché en mémoire.
        """.trimIndent())
        
        log("Prospection terminée. Liste de leads générée.")
        report(createReport(command, ReportStatus.SUCCESS, prospects))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




