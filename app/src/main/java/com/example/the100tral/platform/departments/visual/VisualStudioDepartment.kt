package com.example.the100tral.platform.departments.visual

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Studio Visuel.
 * Responsable du scripting vidéo et de la génération d'assets visuels.
 */
class VisualStudioDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Studio Visuel"
    override val authorityLevel: Int = 3
    override val domain: String = "VISUAL_STUDIO"

    override suspend fun dispatch(command: Command) {
        log("Génération d'assets visuels pour : ${command.instruction}")

        val thought = think("Génère un script vidéo et une description d'assets visuels pour : ${command.instruction}")

        val report = createReport(
            command = command,
            status = ReportStatus.SUCCESS,
            message = thought,
            data = mapOf(
                "creative_output" to thought
            )
        )

        log("Assets générés, transmission au Chef de Projet.")
        commandChain.report(report)
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}







