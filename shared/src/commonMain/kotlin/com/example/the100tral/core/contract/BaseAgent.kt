package com.example.the100tral.core.contract

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import com.example.the100tral.core.persistence.MemoryStorage
import kotlinx.serialization.json.*

/**
 * BaseAgent implémente IAgent. 
 */
abstract class BaseAgent(
    protected val llmService: LLMService? = null,
    protected val memoryStorage: MemoryStorage? = null
) : IAgent {
    abstract override val name: String
    abstract override val authorityLevel: Int
    abstract override val domain: String
    
    private val tools = mutableMapOf<String, ITool>()

    fun registerTool(tool: ITool) {
        tools[tool.toolName] = tool
    }

    protected fun log(message: String, timeMs: Long = 0) {
        println(message)
        ThoughtMonitor.publish(name, domain, message, timeMs = timeMs)
    }

    protected fun consultCorporateMemory(query: String): String {
        val findings = memoryStorage?.searchKnowledge(query) ?: emptyList()
        return if (findings.isNotEmpty()) {
            log("Mémoire collective consultée : ${findings.size} faits pertinents trouvés.")
            findings.joinToString("\n") { "- ${it.content}" }
        } else {
            "Aucune donnée antérieure sur ce sujet."
        }
    }

    protected suspend fun think(task: String): String {
        return thinkAndAct(task)
    }

    protected suspend fun thinkAndAct(task: String, maxIterations: Int = 5): String {
        val startTime = System.currentTimeMillis()
        val sharedContext = consultCorporateMemory(domain)
        val toolDescription = if (tools.isNotEmpty()) {
            "Outils disponibles :\n" + tools.values.joinToString("\n") { 
                "- ${it.toolName}: ${it.description}" 
            }
        } else "Aucun outil disponible."

        var conversationHistory = "Mission : $task\nOutils : $toolDescription\nMémoire : $sharedContext"

        var iterations = 0
        var lastResponse = ""

        while (iterations < maxIterations) {
            log("Réflexion...")
            val response = llmService?.think(conversationHistory) ?: "Erreur."
            lastResponse = response

            if (response.contains("TOOL_CALL:")) {
                val match = Regex("TOOL_CALL:\\s*(\\w+)\\s*(\\{.*\\})").find(response)
                if (match != null) {
                    val toolName = match.groupValues[1]
                    val paramsJson = match.groupValues[2]
                    val params = parseParams(paramsJson)
                    val result = useTool(toolName, params)
                    conversationHistory += "\nAgent: $response\nObservation: ${result.output}"
                    iterations++
                    continue
                }
            }
            break
        }

        log("Pensée terminée.", System.currentTimeMillis() - startTime)
        return lastResponse
    }

    private fun parseParams(json: String): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        try {
            val element = Json.parseToJsonElement(json)
            if (element is JsonObject) {
                element.forEach { (k, v) -> 
                    map[k] = if (v is JsonPrimitive) v.content else v.toString() 
                }
            }
        } catch (_: Exception) { }
        return map
    }

    protected suspend fun useTool(toolName: String, params: Map<String, Any>): ToolResult {
        val tool = tools[toolName] ?: return ToolResult(success = false, output = "Outil '$toolName' non trouvé.")
        log("Exécution de l'outil : $toolName")
        val result = tool.execute(params)
        log("Résultat de l'outil : ${result.output}")
        return result
    }

    protected fun createReport(command: Command, status: ReportStatus, message: String? = null, data: Map<String, Any> = emptyMap()): Report {
        return Report(commandId = command.id, status = status, message = message, data = data)
    }

    // Méthodes de communication (non-interface pour stabilité IR)
    open suspend fun dispatch(command: Command) {
        log("Dispatch : ${command.instruction}")
    }

    open suspend fun report(result: Report) {
        log("Report : ${result.status}")
    }
}
