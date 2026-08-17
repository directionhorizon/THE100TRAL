package com.example.the100tral.core.monitor

import com.example.the100tral.core.contract.AgentThought

object ThoughtMonitor {
    val thoughts = mutableListOf<AgentThought>() // Ouvert pour l'UI
    private var summary: String = "PrÃªt pour l'action."
    private var persistenceListener: ((AgentThought) -> Unit)? = null

    fun setPersistenceListener(listener: (AgentThought) -> Unit) {
        this.persistenceListener = listener
    }

    fun updateThought(agentName: String, domain: String, message: String) {
        val thought = AgentThought(agentName, domain, message)
        thoughts.add(0, thought)
        persistenceListener?.invoke(thought)
    }

    fun updateSummary(newSummary: String) {
        summary = newSummary
    }

    fun getSummary(): String = summary
    fun getRecentThoughts(): List<AgentThought> = thoughts.take(50)
}

