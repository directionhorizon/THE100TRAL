package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent de Communication de Crise (Niveau 4).
 * ProtaƒÂ¨ge l'image de marque et gaƒÂ¨re les relations publiques digitales.
 */
class DigitalReputationAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val agentIdentifier: String = "Agent RaƒÂ©putation & Com de Crise"
    override val authorityLevel: Int = 4
    override val agentDomain: String = "DIGITAL_PR"

    override suspend fun dispatch(command: Command) {
        log("Analyse de l'image de marque pour : " + command.instruction)
        
        val strategy = performAction("Contexte : " + command.instruction + 
            "\nTaƒÂ¢che : Analyse les risques de raƒÂ©putation en utilisant les outils d'aƒÂ©coute sociale et praƒÂ©pare une raƒÂ©ponse officielle protectrice.")
        
        log("StrataƒÂ©gie de daƒÂ©fense gaƒÂ©naƒÂ©raƒÂ©e : " + strategy)
        report(createReport(command, ReportStatus.SUCCESS, strategy))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


