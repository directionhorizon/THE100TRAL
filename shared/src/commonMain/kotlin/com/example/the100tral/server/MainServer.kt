package com.example.the100tral.server

import com.example.the100tral.platform.orchestration.SuperOrchestrator

class MainServer(private val orchestrator: SuperOrchestrator) {

    suspend fun handleRequest(userPrompt: String) {
        orchestrator.initiateMission(userPrompt)
    }
}

