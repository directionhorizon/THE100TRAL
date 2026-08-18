package com.example.the100tral.core.ai.providers

import com.example.the100tral.core.ai.ILLMProvider

class GeminiProvider(val apiKey: String) : ILLMProvider {
    override val providerName: String = "Google Gemini"
    override suspend fun generateContent(prompt: String): String {
        return "RÃ©ponse Gemini simulÃ©e."
    }
}

