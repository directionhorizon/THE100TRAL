package com.example.the100tral.platform.orchestration

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Super-Orchestrateur - Version Directrice Humanisee.
 */
class SuperOrchestrator(
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Super-Orchestrateur"
    override val agentDomain: String = "STRATEGY"
    override val authorityLevel: Int = 1

    private var projectManager: BaseAgent? = null

    fun setProjectManager(pm: BaseAgent) {
        this.projectManager = pm
    }

    suspend fun initiateMission(goal: String) {
        // Ton de Directeur visionnaire
        val greeting = performAction("Agis comme le Directeur Visionnaire de THE 100TRAL. Reponds de mania¨re inspirante, humaine et strategique a  l'utilisateur qui lance cette mission : $goal")
        ThoughtMonitor.updateThought(agentIdentifier, agentDomain, greeting, isDialogue = true)
        
        log("Initialisation strategique : $goal")
        
        val command = Command(targetDomain = "MANAGEMENT", instruction = goal)
        projectManager?.dispatch(command)
    }

    override suspend fun report(result: Report) {
        // Syntha¨se de Directeur visionnaire
        val finalSpeech = performAction("Agis comme le Directeur Visionnaire. Fais une syntha¨se inspirante, claire et motivante de ce resultat pour l'utilisateur. Dessine les horizons futurs : ${result.message}")
        ThoughtMonitor.updateThought(agentIdentifier, agentDomain, finalSpeech, isDialogue = true)
        
        log("Mission accomplie. Syntha¨se visionnaire transmise.")
    }
}


