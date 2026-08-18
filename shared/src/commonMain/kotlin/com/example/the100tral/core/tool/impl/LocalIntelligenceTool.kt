package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import com.example.the100tral.core.network.TavilyService

/**
 * Outil d'intelligence locale focalisé sur le Congo (Sources .cg et médias locaux).
 */
class LocalIntelligenceTool(private val tavilyService: TavilyService) : ITool {
    override val toolName: String = "LOCAL_CONGO_INTELLIGENCE"
    override val description: String = "Analyse les tendances, la culture et les sources média spécifiques au Congo. Paramètre: 'topic'"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val topic = params["topic"] as? String ?: return ToolResult(success = false, output = "Paramètre 'topic' manquant.")
        val localQuery = "$topic Congo Brazzaville Pointe-Noire site:.cg OR site:adiac-congo.com OR site:radiookapi.net"
        val response = tavilyService.search(localQuery)
        
        return if (response != null) {
            ToolResult(success = true, output = "Intelligence locale (Congo) récupérée pour : $topic", data = mapOf("raw_data" to response))
        } else {
            ToolResult(success = false, output = "Échec de la récupération des données locales.")
        }
    }
}



