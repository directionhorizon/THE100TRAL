package com.example.the100tral.core.contract

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.tool.ITool

abstract class BaseAgent(
    protected val llmService: LLMService?,
    protected val memoryStorage: MemoryStorage?
) : IAgent {

    protected val tools = mutableMapOf<String, ITool>()

    fun registerTool(tool: ITool) {
        tools[tool.toolName] = tool
    }

    protected fun log(message: String) {
        println("[$name] $message")
        ThoughtMonitor.updateThought(name, domain, message)
    }

    // Fonction "think" rÃ©clamÃ©e par les agents
    protected suspend fun think(task: String): String {
        return thinkAndAct(task)
    }

    protected suspend fun thinkAndAct(task: String): String {
        val toolDescription = tools.values.joinToString("\n") { "- ${it.toolName}: ${it.description}" }
        val prompt = "Mission: $task\nOutils: $toolDescription"
        return llmService?.think(prompt) ?: "IA hors ligne."
    }

    // Fonction "publish" rÃ©clamÃ©e par certains services
    protected fun publish(message: String) {
        log("Publication: $message")
    }

    open suspend fun dispatch(command: Command) {
        log("Commande reÃ§ue: ${command.instruction}")
    }

    open suspend fun report(result: Report) {
        log("Rapport: ${result.message}")
    }
}
