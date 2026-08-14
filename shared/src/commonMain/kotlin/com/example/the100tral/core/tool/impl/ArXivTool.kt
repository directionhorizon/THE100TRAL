package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import com.example.the100tral.core.network.TavilyService

/**
 * Outil spécialisé pour interroger la base ArXiv via Tavily (filtre scientifique).
 */
class ArXivTool(private val tavilyService: TavilyService) : ITool {
    override val toolName: String = "ARXIV_RESEARCHER"
    override val description: String = "Recherche des papiers scientifiques sur ArXiv concernant l'IA et la gestion de projet. Paramètre: 'query'"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val query = params["query"] as? String ?: return ToolResult(success = false, output = "Paramètre 'query' manquant.")
        val scientificQuery = "site:arxiv.org $query"
        val response = tavilyService.search(scientificQuery)
        
        return if (response != null) {
            ToolResult(success = true, output = "Sources scientifiques ArXiv trouvées pour : $query", data = mapOf("raw_data" to response))
        } else {
            ToolResult(success = false, output = "Aucun résultat trouvé sur ArXiv.")
        }
    }
}
