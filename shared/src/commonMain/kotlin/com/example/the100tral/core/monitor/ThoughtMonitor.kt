package com.example.the100tral.core.monitor

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Singleton pour capturer et diffuser les pensées et actions des agents.
 */
object ThoughtMonitor {
    
    data class AgentThought(
        val agentName: String = "",
        val domain: String = "",
        val message: String = "",
        val sourceAgent: String? = null,
        val executionTimeMs: Long = 0,
        val timestamp: String = "" // Placeholder for KMP
    )

    private val _thoughts = MutableStateFlow<List<AgentThought>>(emptyList())
    val thoughts: StateFlow<List<AgentThought>> = _thoughts.asStateFlow()

    private val _lastResultSummary = MutableStateFlow<String>("")
    val lastResultSummary: StateFlow<String> = _lastResultSummary.asStateFlow()

    // Listener pour la persistance Cloud
    private var persistenceListener: ((AgentThought) -> Unit)? = null

    fun setPersistenceListener(listener: (AgentThought) -> Unit) {
        persistenceListener = listener
    }

    fun publish(agentName: String, domain: String, message: String, source: String? = null, timeMs: Long = 0) {
        val thought = AgentThought(agentName, domain, message, source, timeMs)
        _thoughts.update { current ->
            listOf(thought) + current
        }
        persistenceListener?.invoke(thought)
    }

    fun updateSummary(summary: String) {
        _lastResultSummary.value = summary
        // Sauvegarder aussi le résumé dans le Cloud
        persistenceListener?.invoke(AgentThought("SYSTEM", "SUMMARY", summary))
    }
}
