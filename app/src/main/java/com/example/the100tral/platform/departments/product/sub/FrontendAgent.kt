package com.example.the100tral.platform.departments.product.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class FrontendAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Agent Frontend"
    override val authorityLevel: Int = 4
    override val domain: String = "FRONTEND"

    override suspend fun dispatch(command: Command) {
        log("Création de l'interface utilisateur multiplateforme...")
        val uiCode = thinkAndAct("Génère l'interface Compose pour : ${command.instruction}. Utilise les standards Material 3.")
        
        report(createReport(command, ReportStatus.SUCCESS, "UI conçue : $uiCode"))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}


