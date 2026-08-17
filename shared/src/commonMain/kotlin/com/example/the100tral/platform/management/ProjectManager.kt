package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.monitor.ThoughtMonitor

/**
 * Chef de Projet - VERSION RESTAURÉE.
 * Orchestre les départements et divise les tâches intelligemment.
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
    private val reportBuffer = mutableMapOf<String, MutableList<Report>>()

    fun registerDepartment(domain: String, department: BaseAgent) {
        departments[domain] = department
        log("Département '$domain' opérationnel et relié.")
    }

    override suspend fun dispatch(command: Command) {
        log("Analyse de l'objectif : ${command.instruction}")
        
        // RESTAURATION : Utilisation du TaskSplitter pour diviser le travail
        val splitter = TaskSplitter(llmService ?: return log("ERREUR : IA non disponible."))
        val subCommands = splitter.splitTask(command.instruction, departments.keys.toList(), memoryStorage)

        if (subCommands.isEmpty()) {
            log("Impossible de diviser la tâche. Exécution en mode direct.")
            executeSingleCommand(command)
        } else {
            log("Délégation de ${subCommands.size} sous-tâches aux pôles spécialisés.")
            subCommands.forEach { subCmd ->
                executeSingleCommand(subCmd)
            }
        }
    }

    private suspend fun executeSingleCommand(command: Command) {
        val department = departments[command.targetDomain]
        if (department != null) {
            log("Envoi au pôle ${command.targetDomain} : ${command.instruction}")
            department.dispatch(command)
        } else {
            log("ERREUR : Aucun pôle trouvé pour '${command.targetDomain}'. Échec critique.")
            report(createReport(command, ReportStatus.FAILURE, "Domaine ${command.targetDomain} non relié."))
        }
    }

    override suspend fun report(result: Report) {
        log("Réception d'un livrable du pôle : ${result.commandId}")
        
        // Mise à jour visuelle immédiate
        ThoughtMonitor.updateSummary(result.message ?: "Tâche accomplie.")

        // On remonte l'information au Super-Orchestrateur
        superOrchestratorChain.report(result)
    }
}

