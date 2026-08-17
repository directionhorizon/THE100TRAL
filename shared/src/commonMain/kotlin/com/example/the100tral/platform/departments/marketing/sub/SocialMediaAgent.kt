package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Agent Social Media - VERSION RESTAURÉE.
 * Connecté aux tendances réelles et aux outils de publication.
 */
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
        
        // On demande à l'IA d'utiliser ses outils spécialisés
        val strategy = thinkAndAct("""
            Élabore une stratégie virale pour : ${command.instruction}
            1. Utilise TAVILY_SEARCH pour les tendances actuelles.
            2. Utilise TIKTOK_TREND_TOOL pour l'analyse spécifique vidéo.
            3. Propose un post LinkedIn optimisé.
        """.trimIndent())
        
        // Remontée du rapport stratégique
        report(createReport(command, ReportStatus.SUCCESS, strategy))
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}

