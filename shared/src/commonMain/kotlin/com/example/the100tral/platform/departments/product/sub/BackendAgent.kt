package com.example.the100tral.platform.departments.product.sub

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

class BackendAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Back-end"
    override val authorityLevel: Int = 4
    override val domain: String = "BACKEND"

    override suspend fun dispatch(command: Command) {
        log("Génération de la logique serveur...")
        val thought = think("Génère le schéma de base de données pour : ${command.instruction}")
        
        // Utilisation réelle de l'outil de création de fichier
        useTool("FILE_CREATOR", mapOf(
            "filename" to "schema_${command.id}.sql",
            "content" to "CREATE TABLE ... -- Basé sur : ${command.instruction}"
        ))

        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




