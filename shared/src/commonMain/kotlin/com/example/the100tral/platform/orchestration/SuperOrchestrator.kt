package com.example.the100tral.platform.orchestration

import com.example.the100tral.core.contract.*

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.monitor.ThoughtMonitor

/**
 * Niveau 1 : Autorité Suprême.
 * Définit la vision et arbitre les résultats finaux.
 */
class SuperOrchestrator(
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val name: String = "Super-Orchestrateur"
    override val authorityLevel: Int = 1
    override val domain: String = "GLOBAL_STRATEGY"

    private var projectManager: BaseAgent? = null
    private var executiveAssistant: BaseAgent? = null

    fun setProjectManager(pm: BaseAgent) {
        this.projectManager = pm
    }

    fun setExecutiveAssistant(ea: BaseAgent) {
        this.executiveAssistant = ea
    }

    suspend fun initiateMission(goal: String, targetDomain: String) {
        log("Initialisation de la vision stratégique : $goal")
        ThoughtMonitor.updateSummary("Nouvelle mission : $goal")
        
        // On demande à la secrétaire de noter le lancement de la mission
        executiveAssistant?.dispatch(Command(targetDomain = "EXECUTIVE_SUPPORT", instruction = "Note le lancement de la mission : $goal"))

        val command = Command(
            targetDomain = targetDomain,
            instruction = goal,
            priority = 1
        )
        
        dispatch(command)
    }

    override suspend fun dispatch(command: Command) {
        log("Délégation de la mission au Chef de Projet.")
        projectManager?.dispatch(command) ?: log("ERREUR : Aucun Chef de Projet configuré.")
    }

    override suspend fun report(result: Report) {
        log("RESULTAT FINAL RECU : ${result.status} | Message: ${result.message}")
        log("Données du livrable : ${result.data}")
        
        // Mise à jour de la synthèse finale pour l'affichage GPS
        val summary = if (result.status == ReportStatus.SUCCESS) {
            "MISSION ACCOMPLIE : ${result.message}"
        } else {
            "ÉCHEC DE MISSION : ${result.message}"
        }
        ThoughtMonitor.updateSummary(summary)

        // Exportation automatique vers Notion pour archivage permanent
        if (result.status == ReportStatus.SUCCESS) {
            useTool("NOTION_CONNECTOR", mapOf(
                "action" to "ARCHIVE_MISSION",
                "data" to "Mission : ${result.message}\nDonnées : ${result.data}"
            ))
        }
    }
}
