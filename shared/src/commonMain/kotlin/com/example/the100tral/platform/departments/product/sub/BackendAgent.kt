package com.example.the100tral.platform.departments.product.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class BackendAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Agent Backend"
    override val authorityLevel: Int = 4
    override val domain: String = "BACKEND"

    override suspend fun dispatch(command: Command) {
        log("Conception de l'architecture serveur et API...")
        val code = thinkAndAct("""
            Génère la logique backend pour : ${command.instruction}
            1. Utilise FILE_CREATION_TOOL pour créer les fichiers nécessaires.
            2. Définis les modèles de données KMP.
        """.trimIndent())
        
        report(createReport(command, ReportStatus.SUCCESS, "Backend prêt : $code"))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}
