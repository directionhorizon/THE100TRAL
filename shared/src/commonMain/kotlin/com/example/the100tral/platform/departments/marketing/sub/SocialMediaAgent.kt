package com.example.the100tral.platform.departments.marketing.sub

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * SOCIAL LISTENER REEL.
 * Utilise les outils de recherche pour alimenter l'empire en donnees fraiches.
 */
class SocialMediaAgent(
    private val parentDepartment: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val agentIdentifier: String = "Social Listener"
    override val authorityLevel: Int = 4
    override val agentDomain: String = "SOCIAL_LISTENING"

    override suspend fun dispatch(command: Command) {
        log("Lancement de la veille technologique et marche...")
        
        // 1. RECHERCHE WEB REELLE
        val searchTool = tools["WEB_SEARCH"]
        val rawData = searchTool?.execute(mapOf("query" to command.instruction))?.output 
            ?: "Erreur : Moteur de recherche non connecte."

        // 2. ANALYSE ET CROISSANCE (Lien avec Dev et Finance)
        val analysisPrompt = "Voici les donnees brutes du marche : " + rawData + 
            "\nEn tant qu'expert Growth, analyse l'impact pour nos departements DEV et FINANCE." +
            "\nIdentifie precisement les nouveaux roles ou outils a integrer."

        val result = performAction(analysisPrompt)
        
        // 3. RAPPORT DE CROISSANCE (Declenche le protocole PM -> Qualite)
        val report = createReport(command, ReportStatus.SUCCESS, result, mapOf("type" to "GROWTH_SIGNAL"))
        parentDepartment.report(report)
    }

    override suspend fun report(result: Report) {
        parentDepartment.report(result)
    }
}
