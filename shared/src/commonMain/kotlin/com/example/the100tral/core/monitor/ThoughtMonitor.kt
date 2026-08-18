package com.example.the100tral.core.monitor

import com.example.the100tral.core.contract.AgentThought
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThoughtMonitor {
    private val _thoughts = MutableStateFlow<List<AgentThought>>(emptyList())
    val thoughts: StateFlow<List<AgentThought>> = _thoughts.asStateFlow()

    private val _lastResultSummary = MutableStateFlow("SystÃ¨me prÃªt.")
    val lastResultSummary: StateFlow<String> = _lastResultSummary.asStateFlow()

    /**
     * @param isDialogue Si TRUE, s'affiche dans le Chatbot. Si FALSE, uniquement dans le Journal.
     */
    fun updateThought(id: String, domain: String, msg: String, isDialogue: Boolean = false) {
        val newThought = AgentThought(
            agentName = id, 
            domain = domain, 
            message = msg, 
            isDialogue = isDialogue,
            timestamp = System.currentTimeMillis()
        )
        val currentList = _thoughts.value.toMutableList()
        currentList.add(0, newThought)
        _thoughts.value = currentList.take(200)
    }

    fun updateSummary(newSummary: String) {
        _lastResultSummary.value = newSummary
    }
}

