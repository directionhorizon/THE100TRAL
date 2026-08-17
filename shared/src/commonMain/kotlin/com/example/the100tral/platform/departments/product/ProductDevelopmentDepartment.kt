package com.example.the100tral.platform.departments.product

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Pôle Produit (Niveau 3).
 * Gère le cycle de vie logiciel, du code à la mise en production.
 */
class ProductDevelopmentDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {
    
    override val name: String = "Pôle Produit"
    override val authorityLevel: Int = 3
    override val domain: String = "PRODUCT_DEV"

    private val subAgents = mutableMapOf<String, BaseAgent>()

    fun registerSubAgent(domain: String, agent: BaseAgent) {
        subAgents[domain] = agent
        log("Sous-agent produit enregistré : $domain")
    }

    override suspend fun dispatch(command: Command) {
        log("Réception d'une mission de développement : ${command.instruction}")
        
        // Délégation intelligente : on transmet l'ordre à tous les sous-agents concernés
        subAgents.values.forEach { it.dispatch(command) }
    }

    override suspend fun report(result: Report) {
        log("Rapport technique reçu (${result.status}). Remontée au Manager.")
        commandChain.report(result)
    }
}

