package com.example.the100tral.server

import com.example.the100tral.platform.orchestration.SuperOrchestrator

/**
 * Serveur Principal agissant comme Passerelle (Gateway).
 */
class MainServer(private val superOrchestrator: SuperOrchestrator) {
    
    suspend fun handleUserRequest(request: String, domain: String = "PRODUCT_DEV") {
        println("[Serveur] Requête client reçue : $request")
        superOrchestrator.initiateMission(request, domain)
    }
}

