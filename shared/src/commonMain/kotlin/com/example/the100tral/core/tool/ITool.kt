package com.example.the100tral.core.tool

/**
 * Interface pour tous les outils exploitables par les agents IA.
 */
interface ITool {
    val toolName: String
    val description: String
    
    /**
     * Exécute l'outil avec les paramètres fournis.
     */
    suspend fun execute(params: Map<String, Any>): ToolResult
}

data class ToolResult(
    val success: Boolean,
    val output: String,
    val data: Map<String, Any> = emptyMap()
)

