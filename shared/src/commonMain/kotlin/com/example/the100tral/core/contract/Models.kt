package com.example.the100tral.core.contract

// Removed java.util.UUID for KMP compatibility

/**
 * Représente une instruction descendant la chaîne de commandement.
 * Changée en classe simple avec copy() manuel pour éviter un bug du compilateur Kotlin IR (ICE).
 */
class Command(
    val id: String = "",
    val targetDomain: String,
    val instruction: String,
    val payload: Map<String, Any> = emptyMap(),
    val priority: Int = 0,
) {
    fun copy(
        id: String = this.id,
        targetDomain: String = this.targetDomain,
        instruction: String = this.instruction,
        payload: Map<String, Any> = this.payload,
        priority: Int = this.priority,
    ) = Command(id, targetDomain, instruction, payload, priority)
}

/**
 * Représente un résultat ou une erreur remontant la chaîne de commandement.
 */
class Report(
    val commandId: String,
    val status: ReportStatus,
    val data: Map<String, Any> = emptyMap(),
    val message: String? = null,
) {
    fun copy(
        commandId: String = this.commandId,
        status: ReportStatus = this.status,
        data: Map<String, Any> = this.data,
        message: String? = this.message,
    ) = Report(commandId, status, data, message)
}

enum class ReportStatus {
    SUCCESS, FAILURE, IN_PROGRESS
}
