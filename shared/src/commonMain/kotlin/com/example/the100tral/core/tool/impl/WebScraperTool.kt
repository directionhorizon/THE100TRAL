package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import kotlinx.coroutines.delay

/**
 * Outil permettant aux agents d'extraire des informations d'une URL.
 */
class WebScraperTool : ITool {
    override val toolName: String = "WebScraper"
    override val description: String = "Extrait le texte d'un site web. Paramètre: url"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val url = params["url"]?.toString() ?: return ToolResult(success = false, output = "URL manquante.")
        
        // Simulation d'une requête réseau
        delay(1000) 
        
        // Simulation de contenu extrait
        val simulatedContent = """
            [CONTENU EXTRAIT DE $url]
            Nouveautés du secteur : L'IA générative révolutionne le marketing digital. 
            Concurrent A a baissé ses prix de 20%.
            Tendances 2026 : Automatisation complète des chaînes de commandement.
        """.trimIndent()

        return ToolResult(success = true, output = "Contenu extrait avec succès de $url", data = mapOf("text" to simulatedContent))
    }
}

