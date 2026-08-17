package com.example.the100tral.core.contract

import kotlinx.serialization.Serializable

@Serializable
data class Command(
    val id: String = "CMD_${System.currentTimeMillis()}",
    val targetDomain: String,
    val instruction: String,
    val priority: Int = 3
)

enum class ReportStatus {
    SUCCESS, FAILURE, PENDING, IN_PROGRESS
}

@Serializable
data class Report(
    val commandId: String,
    val status: ReportStatus,
    val message: String,
    val data: Map<String, String> = emptyMap()
)

@Serializable
data class AgentThought(
    val agentName: String,
    val domain: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val executionTimeMs: Long = 0 
)

