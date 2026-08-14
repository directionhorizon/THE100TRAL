package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.monitor.ThoughtMonitor
// Removed java.util.UUID

/**
 * Niveau 2 : Chef de Projet.
 * Orchestre les départements spécialisés et arbitre les conflits.
 */
class ProjectManager(
    private val superOrchestratorChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Chef de Projet"
    override val authorityLevel: Int = 2
    override val domain: String = "MANAGEMENT"

    private val departments = mutableMapOf<String, BaseAgent>()
    private val arbitrator = ConflictArbitrator(llmService)
    
    // Accumulateur de rapports pour arbitrage (simplifié)
    private val reportBuffer = mutableMapOf<String, MutableList<Report>>()

    fun registerDepartment(domain: String, department: BaseAgent) {
        departments[domain] = department
        log("Département enregistré : $domain")
    }

    enum class PresentationMode { EXECUTIVE, TECHNICAL, STRATEGIC }

    suspend fun generateCompanyPresentation(mode: PresentationMode): String {
        val task = when (mode) {
            PresentationMode.EXECUTIVE -> "Présente l'entreprise THE 100TRAL sous l'angle du ROI et de la performance financière."
            PresentationMode.TECHNICAL -> "Présente l'entreprise sous l'angle de son architecture IA hiérarchique et technique."
            PresentationMode.STRATEGIC -> "Présente l'entreprise sous l'angle de sa vision et de sa veille culturelle."
        }
        return think(task)
    }

    override suspend fun dispatch(command: Command) {
        log("Réception de l'objectif stratégique. Analyse pour subdivision éventuelle...")
        
        val splitter = llmService?.let { TaskSplitter(it) }
        val subCommands = if (splitter != null) {
            splitter.splitTask(command.instruction, departments.keys.toList(), memoryStorage)
        } else {
            listOf(command)
        }

        if (subCommands.isEmpty()) {
            log("Aucune sous-tâche générée. Utilisation de la commande brute.")
            executeSingleCommand(command)
        } else {
            log("${subCommands.size} sous-tâche(s) identifiée(s). Distribution...")
            subCommands.forEach { subCmd ->
                executeSingleCommand(subCmd)
            }
        }
    }

    private suspend fun executeSingleCommand(command: Command) {
        val department = departments[command.targetDomain]
        if (department != null) {
            log("Délégation au département : ${command.targetDomain} -> ${command.instruction}")
            department.dispatch(command)
        } else {
            log("ERREUR : Aucun département trouvé pour le domaine ${command.targetDomain}")
            report(createReport(command, ReportStatus.FAILURE, "Domaine inconnu"))
        }
    }

    override suspend fun report(result: Report) {
        log("Rapport reçu du département (${result.status}).")
        
        if (result.status == ReportStatus.FAILURE) {
            analyzeAndLearnFromFailure(result)
        }

        val reports = reportBuffer.getOrPut(result.commandId) { mutableListOf() }
        reports.add(result)
        
        // Mise à jour de la synthèse dans le moniteur (pour l'UI GPS)
        ThoughtMonitor.updateSummary(result.message ?: "Action opérationnelle terminée avec succès.")

        if (reports.isNotEmpty()) { 
            log("Arbitrage des rapports pour la commande ${result.commandId}...")
            val finalReport = arbitrator.arbitrate(reports)
            log("Arbitrage terminé. Transmission au Super-Orchestrateur.")
            superOrchestratorChain.report(finalReport)
            reportBuffer.remove(result.commandId)
        }
    }

    /**
     * Analyse un échec pour optimiser les futurs ordres.
     */
    private suspend fun analyzeAndLearnFromFailure(report: Report) {
        log("AUTO-OPTIMISATION : Analyse de la cause de l'échec...")
        
        val analysis = think("""
            L'agent a échoué avec le message : ${report.message}
            Les données transmises étaient : ${report.data}
            
            Quelle est la leçon à en tirer ? Donne une consigne courte pour éviter cet échec la prochaine fois.
        """.trimIndent())

        memoryStorage?.saveKnowledge(
            com.example.the100tral.core.persistence.MemoryEntry(
                domain = "MANAGEMENT_OPTIMIZATION",
                content = analysis,
                source = "SELF_LEARNING"
            )
        )
        log("Leçon apprise et mémorisée : $analysis")
    }
}

