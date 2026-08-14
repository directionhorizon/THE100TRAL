package com.example.the100tral.core.ai.providers

import com.example.the100tral.core.ai.ILLMProvider

/**
 * Fournisseur pour les LLM hébergés localement.
 */
class LocalLLMProvider(
    override val providerName: String,
    private val localUrl: String
) : ILLMProvider {

    override suspend fun generateContent(prompt: String): String {
        // Simulation d'un appel à un serveur local (ex: Ollama ou LM Studio)
        return "[Pensée via $providerName @ $localUrl] : Réponse simulée pour : $prompt"
    }
}
