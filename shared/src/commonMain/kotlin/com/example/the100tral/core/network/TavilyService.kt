package com.example.the100tral.core.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Serializable
private data class TavilySearchRequest(
    @SerialName("api_key")
    val apiKey: String,
    val query: String,
    @SerialName("search_depth")
    val searchDepth: String,
    @SerialName("include_answer")
    val includeAnswer: Boolean,
)

/**
 * Service pour interagir avec l'API Tavily.ai.
 * Offre des capacités de recherche web, d'extraction et d'analyse en temps réel.
 */
class TavilyService(private val apiKey: String) {

    private val client = HttpClient()

    suspend fun search(query: String, searchDepth: String = "basic"): String? {
        val requestBody = TavilySearchRequest(
            apiKey = apiKey,
            query = query,
            searchDepth = searchDepth,
            includeAnswer = true
        )

        return try {
            val response = client.post("https://api.tavily.com/search") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(requestBody))
            }
            if (response.status.isSuccess()) {
                response.bodyAsText()
            } else {
                "Erreur Tavily: ${response.status.value}"
            }
        } catch (e: Exception) {
            "Erreur de connexion Tavily: ${e.message}"
        }
    }
}

