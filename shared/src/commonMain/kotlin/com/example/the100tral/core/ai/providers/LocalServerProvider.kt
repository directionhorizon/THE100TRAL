package com.example.the100tral.core.ai.providers

import com.example.the100tral.core.ai.ILLMProvider
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

/**
 * FOURNISSEUR RÉEL POUR LM STUDIO / PINOKIO.
 * Effectue des appels HTTP JSON réels.
 */
class LocalServerProvider(
    override val providerName: String,
    private val endpointUrl: String
) : ILLMProvider {

    private val client = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun generateContent(prompt: String): String {
        return try {
            // APPEL RÉEL AU SERVEUR LOCAL (Format OpenAI)
            val response: HttpResponse = client.post(endpointUrl + "/chat/completions") {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    put("model", "local-model")
                    putJsonArray("messages") {
                        addJsonObject {
                            put("role", "user")
                            put("content", prompt)
                        }
                    }
                    put("temperature", 0.7)
                }.toString())
            }

            if (response.status == HttpStatusCode.OK) {
                val body = response.bodyAsText()
                val jsonResponse = json.parseToJsonElement(body).jsonObject
                jsonResponse["choices"]?.jsonArray?.get(0)?.jsonObject?.get("message")?.jsonObject?.get("content")?.jsonPrimitive?.content 
                    ?: "Erreur: Format de réponse inconnu."
            } else {
                "ERREUR : Le serveur " + providerName + " a répondu avec le code " + response.status
            }
        } catch (e: Exception) {
            "ERREUR CRITIQUE : Impossible de joindre " + providerName + " sur " + endpointUrl + ". Vérifiez que le serveur est démarré."
        }
    }
}
