package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService

import com.example.the100tral.core.persistence.MemoryStorage

class SocialMediaAgent(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Agent Social Media"
    override val authorityLevel: Int = 4
    override val domain: String = "SOCIAL_MEDIA"

    override suspend fun dispatch(command: Command) {
        log("Analyse des tendances et multi-publication sociale...")
        val thought = thinkAndAct("Génère un plan de contenu multi-réseaux pour : ${command.instruction}. Utilise la recherche web pour identifier les tendances actuelles liées à ce sujet.")
        
        // Publication LinkedIn
        useTool("LINKEDIN_PUBLISHER", mapOf("content" to thought))
        
        // Analyse TikTok
        useTool("TIKTOK_TREND_TOOL", emptyMap())

        // Archivage dans Notion
        useTool("NOTION_CONNECTOR", mapOf(
            "action" to "ARCHIVE_SOCIAL_STRATEGY",
            "data" to thought
        ))

        report(createReport(command, ReportStatus.SUCCESS, thought))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}




