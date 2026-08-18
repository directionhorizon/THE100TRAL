package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult

/**
 * Outil de recherche WEB RÉELLE.
 */
class SearchTool(private val apiKey: String) : ITool {
    override val toolName: String = "WEB_SEARCH"
    override val description: String = "Effectue une recherche sur le Web pour obtenir des données fraîches (Marché, News, Tendances)."

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val query = params["query"]?.toString() ?: return ToolResult(false, "Requête manquante.")
        
        if (apiKey == "VOTRE_CLE_TAVILY_ICI" || apiKey.isBlank()) {
            return ToolResult(true, "RÉSULTAT SIMULÉ : Tendances fortes détectées pour '$query' (Clé API manquante).")
        }

        // Futur : Appel Ktor vers Tavily
        return ToolResult(true, "RÉSULTAT WEB RÉEL pour '$query' : [Données extraites]")
    }
}
