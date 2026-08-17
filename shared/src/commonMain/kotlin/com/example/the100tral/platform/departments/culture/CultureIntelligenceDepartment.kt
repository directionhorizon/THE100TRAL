package com.example.the100tral.platform.departments.culture

import com.example.the100tral.core.contract.*
import kotlinx.coroutines.delay

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage

/**
 * Département Veille & Culture.
 * Responsable du Social Listening, de la curation et de la veille stratégique.
 */
class CultureIntelligenceDepartment(
    private val commandChain: BaseAgent,
    llmService: LLMService? = null,
    memoryStorage: MemoryStorage? = null
) : BaseAgent(llmService, memoryStorage) {

    override val name: String = "Département Veille & Culture"
    override val authorityLevel: Int = 3
    override val domain: String = "WATCH_CULTURE"

    override suspend fun dispatch(command: Command) {
        log("Lancement du Social Listening pour : ${command.instruction}")

        val thought = think("Identifie les tendances actuelles et signaux faibles pour : ${command.instruction}")

        val report = createReport(
            command = command,
            status = ReportStatus.SUCCESS,
            message = thought,
            data = mapOf(
                "social_listening_report" to thought
            )
        )

        log("Veille stratégique terminée, envoi du rapport.")
        commandChain.report(report)
    }

    override suspend fun report(result: Report) {
        commandChain.report(result)
    }
}





