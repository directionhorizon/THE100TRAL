package com.example.the100tral.core.contract

import com.example.the100tral.core.monitor.ThoughtMonitor

object AgentUtils {
    fun log(identifier: String, domain: String, message: String, isDialogue: Boolean = false) {
        println("[" + identifier + "] " + message)
        ThoughtMonitor.updateThought(identifier, domain, message, isDialogue)
    }
}

