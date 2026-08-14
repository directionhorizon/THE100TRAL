package com.example.the100tral.core.ai.providers

import com.example.the100tral.core.ai.ILLMProvider
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

actual class GeminiProvider actual constructor(private val apiKey: String) : ILLMProvider {
    actual override val providerName: String = "Google Gemini (REST)"
    private val client = HttpClient()

    actual override suspend fun generateContent(prompt: String): String {
        return try {
            val response = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    putJsonArray("contents") {
                        addJsonObject {
                            putJsonArray("parts") {
                                addJsonObject { put("text", prompt) }
                            }
                        }
                    }
                }.toString())
            }
            if (response.status.isSuccess()) {
                val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                body["candidates"]?.jsonArray?.firstOrNull()?.jsonObject?.get("content")?.jsonObject?.get("parts")?.jsonArray?.firstOrNull()?.jsonObject?.get("text")?.jsonPrimitive?.content 
                    ?: "Erreur d'analyse de la réponse."
            } else {
                "Erreur API Gemini: ${response.status}"
            }
        } catch (e: Exception) {
            "Erreur de connexion Gemini: ${e.message}"
        }
    }
}
