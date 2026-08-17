package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import kotlinx.coroutines.delay

/**
 * Outil simulant le lancement d'un build pour l'agent DevOps.
 */
class BuildTool : ITool {
    override val toolName: String = "BUILD_LAUNCHER"
    override val description: String = "Simule la compilation et le test d'un projet."

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val projectName = params["project"] as? String ?: "DefaultProject"
        
        delay(3000) // Simulation temps de build
        
        return ToolResult(success = true, output = "Build du projet '$projectName' réussi. Tests unitaires : 100% passés.")
    }
}

