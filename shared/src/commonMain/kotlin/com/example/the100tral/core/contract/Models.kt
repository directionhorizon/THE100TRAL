package com.example.the100tral.core.contract

/**
 * Modèles de données pour un système réel et interdépendant.
 */
class Command(
    val id: String = "CMD_" + System.currentTimeMillis(),
    val targetDomain: String = "GLOBAL",
    val instruction: String = "",
    val priority: Int = 3,
    val attachments: List<String> = emptyList()
)

enum class ReportStatus {
    SUCCESS, FAILURE, PENDING, IN_PROGRESS
}

class Report(
    val commandId: String,
    val status: ReportStatus,
    val message: String,
    val agentDomain: String = "UNKNOWN", // AJOUT POUR LA HIERARCHIE
    val data: Map<String, String> = emptyMap()
)

class AgentThought(
    val agentName: String = "Inconnu",
    val domain: String = "Global",
    val message: String = "",
    val isDialogue: Boolean = false,
    val timestamp: Long = 0L,
    val executionTimeMs: Long = 0L
)

interface AgentData {
    val agentIdentifier: String
    val agentDomain: String
    val authorityLevel: Int
    var preferredModel: String
}

