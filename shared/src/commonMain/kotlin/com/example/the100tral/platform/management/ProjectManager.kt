package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import kotlinx.coroutines.*

class ProjectManager(
    private val superOrchestrator: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val agentIdentifier: String = "Chef de Projet"
    override val authorityLevel: Int = 2
    override val agentDomain: String = "MANAGEMENT"

    private val departments = mutableMapOf<String, BaseAgent>()
    private var qualityAgent: BaseAgent? = null
    private var executiveAssistant: BaseAgent? = null
    
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun setQualityAgent(agent: BaseAgent) { this.qualityAgent = agent }
    fun setExecutiveAssistant(agent: BaseAgent) { this.executiveAssistant = agent }
    fun registerDepartment(domain: String, dept: BaseAgent) { departments[domain] = dept }

    override suspend fun dispatch(command: Command) {
        log("Analyse et dÃ©ploiement des pÃ´les...")
        val splitter = TaskSplitter(llmService, memoryStorage)
        val tasks = splitter.splitTask(command.instruction, departments.keys.toList())

        // EXÃ‰CUTION ASYNCHRONE ET PARALLÃˆLE
        tasks.forEach { subCmd ->
            scope.launch {
                val dept = departments[subCmd.targetDomain]
                dept?.dispatch(subCmd)
            }
        }
    }

    override suspend fun report(result: Report) {
        when {
            result.data["type"] == "GROWTH_SIGNAL" -> {
                log("Signal de croissance détecté. Mandat envoyé à l'Agent Qualité.")
                qualityAgent?.dispatch(Command(targetDomain = "QUALITY", instruction = result.message))
            }
            result.agentDomain == "QUALITY_CONTROL" -> {
                log("Rapport Qualité validé. Envoi au Secrétariat.")
                executiveAssistant?.report(result)
            }
            else -> superOrchestrator.report(result)
        }
    }
}
