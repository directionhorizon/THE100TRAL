package com.example.the100tral.platform.departments.product

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Produit & Développement.
 * Niveau 3 : Orchestrateur technique pour le Niveau 4.
 */
class ProductDevelopmentDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Département Produit & Développement"
    override val authorityLevel: Int = 3
    override val domain: String = "PRODUCT_DEV"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Sous-agent Niveau 4 enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Réception d'une mission complexe. Subdivision en micro-tâches...")
        
        // Délégation automatique aux sous-agents concernés
        subAgents.values.forEach { agent ->
            agent.dispatch(command)
        }
    }

    override suspend fun report(result: Report) {
        log("Rapport reçu d'un sous-agent (${result.status}). Transmission au Chef de Projet.")
        commandChain.report(result)
    }
}




