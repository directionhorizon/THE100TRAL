package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.network.TavilyService
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult

/**
 * Outil de surveillance des réseaux sociaux et du web.
 * Utilise Tavily pour détecter les bruits numériques réels.
 */
class SocialListeningTool(private val tavilyService: TavilyService) : ITool {
    override val toolName: String = "SocialListener"
    override val description: String = "Scanne le web pour détecter le sentiment autour d'une marque ou d'un sujet. Paramètre: topic"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val topic = params["topic"]?.toString() ?: "THE 100TRAL"
        
        // On demande à Tavily une analyse de contexte/sentiment via une recherche approfondie
        val response = tavilyService.search("What is the current public sentiment and latest news about $topic?", "advanced")
            ?: return ToolResult(success = false, output = "Impossible de scanner le web pour $topic")

        return ToolResult(success = true, output = "Analyse web terminée pour $topic. Données brutes récupérées.", data = mapOf("raw_data" to response))
    }
}



