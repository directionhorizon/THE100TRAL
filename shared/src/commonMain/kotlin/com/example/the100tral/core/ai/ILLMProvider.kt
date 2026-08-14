package com.example.the100tral.core.ai

/**
 * Interface pour les fournisseurs de modèles de langage.
 */
interface ILLMProvider {
    val providerName: String
    suspend fun generateContent(prompt: String): String
}
