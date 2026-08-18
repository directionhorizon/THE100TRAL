package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.monitor.ThoughtMonitor

class ProjectManager(
    private val superOrchestratorChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val agentIdentifier: String = "Chef de Projet"
    override val authorityLevel: Int = 2
    override val agentDomain: String = "MANAGEMENT"

    private val departments = mutableMapOf<String, BaseAgent>()
    private var qualityAgent: BaseAgent? = null
    private var executiveAssistant: BaseAgent? = null

    fun setQualityAgent(agent: BaseAgent) { this.qualityAgent = agent }
    fun setExecutiveAssistant(agent: BaseAgent) { this.executiveAssistant = agent }

    fun registerDepartment(domain: String, department: BaseAgent) {
        departments[domain] = department
    }

    override suspend fun dispatch(command: Command) {
        log("Analyse de l'objectif : " + command.instruction)
        
        val splitter = TaskSplitter(llmService, memoryStorage)
        val subCommands = splitter.splitTask(command.instruction, departments.keys.toList())

        for (subCmd in subCommands) {
            val dept = departments[subCmd.targetDomain]
            dept?.dispatch(subCmd)
        }
    }

    override suspend fun report(result: Report) {
        // GESTION DU PROTOCOLE D'ÉVOLUTION DE RÔLE
        if (result.data["type"] == "STRUCTURAL_EVOLUTION") {
            log("Alerte de croissance reçue du Social Listener. Transmission au Contrôleur Qualité.")
            val cmd = Command(targetDomain = "QUALITY", instruction = result.message)
            qualityAgent?.dispatch(cmd)
            return
        }

        // GESTION DU RETOUR QUALITÉ
        if (result.commandId.contains("QUALITY")) {
            log("Analyse Qualité reçue. Transmission des données stratégiques à la Secrétaire.")
            executiveAssistant?.report(result)
            return
        }

        superOrchestratorChain.report(result)
    }
}
