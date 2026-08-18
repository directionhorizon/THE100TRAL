package com.example.the100tral.platform.management

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * SECRÉTAIRE : Gère la file d'attente pour le Directeur.
 */
class ExecutiveAssistant(
    private val orchestrator: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    override val agentIdentifier: String = "Secrétaire"
    override val authorityLevel: Int = 2
    override val agentDomain: String = "EXECUTIVE_SUPPORT"

    private val mutex = Mutex()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override suspend fun dispatch(command: Command) {
        log("Dossier stratégique mis en file d'attente.")
    }

    override suspend fun report(result: Report) {
        // GESTION ASYNCHRONE DE LA TRANSMISSION
        scope.launch {
            mutex.withLock {
                log("Le Directeur est disponible. Transmission du rapport : " + result.commandId)
                delay(1000) // Simulation de lecture du dossier
                orchestrator.report(result)
            }
        }
    }
}
