package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.network.TavilyService
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import kotlinx.serialization.json.*

/**
 * Outil de recherche web propulsé par Tavily.ai.
 * Permet aux agents d'obtenir des informations en temps réel sur le web.
 */
class TavilySearchTool(private val tavilyService: TavilyService) : ITool {
    override val toolName: String = "TAVILY_SEARCH"
    override val description: String = "Recherche sur le web en temps réel. Paramètres: query, depth (basic/advanced)"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val query = params["query"]?.toString() ?: return ToolResult(success = false, output = "Requête manquante")
        val depth = params["depth"]?.toString() ?: "basic"

        val response = tavilyService.search(query, depth) ?: return ToolResult(success = false, output = "Aucune réponse de Tavily")

        return try {
            val jsonResponse = Json.parseToJsonElement(response).jsonObject
            val answer = jsonResponse["answer"]?.jsonPrimitive?.content ?: ""
            val results = jsonResponse["results"]?.jsonArray
            
            val message = if (answer.isNotEmpty()) {
                "Réponse directe : $answer"
            } else {
                "Recherche effectuée pour '$query'. ${results?.size ?: 0} résultats trouvés."
            }

            ToolResult(success = true, output = message, data = mapOf("raw_json" to response))
        } catch (e: Exception) {
            ToolResult(success = false, output = "Erreur lors du parsing de la réponse Tavily: ${e.message}")
        }
    }
}



