package com.example.the100tral.core.contract

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.tool.ITool

abstract class BaseAgent(
    open val llmService: LLMService? = null,
    open val memoryStorage: MemoryStorage? = null
) : AgentData {

    override var preferredModel: String = "GEMINI"
    val tools: MutableMap<String, ITool> = mutableMapOf()

    fun registerTool(tool: ITool) {
        tools[tool.toolName] = tool
    }

    protected fun log(message: String, isDialogue: Boolean = false) {
        AgentUtils.log(agentIdentifier, agentDomain, message, isDialogue)
    }

    /**
     * REFLEXION AVEC GESTION DES FICHIERS JOINTS (TROMBONE).
     */
    protected suspend fun performAction(task: String, attachments: List<String> = emptyList()): String {
        var fullPrompt = task
        if (attachments.isNotEmpty()) {
            fullPrompt += "\n[ANALYSE DE DOCUMENTS JOINTS] : " + attachments.joinToString(", ")
        }
        return llmService?.performAction(fullPrompt, providerKey = preferredModel) ?: "IA non disponible."
    }

    protected fun createReport(command: Command, status: ReportStatus, message: String, data: Map<String, String> = emptyMap()): Report {
        return Report(command.id, status, message, agentDomain, data)
    }

    open suspend fun dispatch(command: Command) {
        log("Instruction : " + command.instruction)
    }

    open suspend fun report(result: Report) {
        log("Rapport du pole : " + result.agentDomain)
    }
}
