package com.example.the100tral.core.ai.providers

import com.example.the100tral.core.ai.ILLMProvider

expect class GeminiProvider(apiKey: String) : ILLMProvider {
    override val providerName: String
    override suspend fun generateContent(prompt: String): String
}

