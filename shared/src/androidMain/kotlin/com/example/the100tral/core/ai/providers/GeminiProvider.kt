package com.example.the100tral.core.ai.providers

import com.example.the100tral.core.ai.ILLMProvider
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

actual class GeminiProvider actual constructor(private val apiKey: String) : ILLMProvider {
    actual override val providerName: String = "Google Gemini"

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    actual override suspend fun generateContent(prompt: String): String {
        return try {
            val response = model.generateContent(
                content { text(prompt) }
            )
            response.text ?: "Aucune réponse de Gemini."
        } catch (e: Exception) {
            "Erreur Gemini: ${e.message}"
        }
    }
}
