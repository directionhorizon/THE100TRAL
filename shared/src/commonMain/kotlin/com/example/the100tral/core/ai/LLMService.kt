package com.example.the100tral.core.ai

import com.example.the100tral.core.security.SecurityGuard

/**
 * Routeur intelligent entre les différents fournisseurs de LLM.
 */
open class LLMService {

    private val providers = mutableMapOf<String, ILLMProvider>()
    private var defaultProvider: String? = null

    fun registerProvider(key: String, provider: ILLMProvider, isDefault: Boolean = false) {
        providers[key] = provider
        if (isDefault || defaultProvider == null) {
            defaultProvider = key
        }
    }

    /**
     * Fait réfléchir l'agent en utilisant le fournisseur spécifié ou celui par défaut.
     */
    suspend fun think(prompt: String, providerKey: String? = null): String {
        val safePrompt = SecurityGuard.filterInput(prompt)
        
        val key = providerKey ?: defaultProvider
        val provider = providers[key]

        if (provider == null) {
            return "ERREUR: Aucun fournisseur LLM disponible."
        }

        val response = provider.generateContent(safePrompt)
        return SecurityGuard.filterOutput(response)
    }

    fun getAvailableProviders(): List<String> = providers.keys.toList()
}

