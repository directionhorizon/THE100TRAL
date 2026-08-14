package com.example.the100tral

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.the100tral.ui.MainContainer
import com.example.the100tral.ui.theme.THE100TRALTheme
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.security.SecureSecretStore
import com.example.the100tral.server.MainServer
import com.example.the100tral.core.persistence.CloudBridgeProvider
import com.example.the100tral.core.monitor.ThoughtMonitor
import kotlinx.coroutines.launch

fun main() {
    // Déclenchement de la migration Cloud avant d'ouvrir la fenêtre
    CloudBridgeProvider.migrateAll()

    // Automatisation Firebase : Liaison du moniteur au Cloud
    ThoughtMonitor.setPersistenceListener {
        CloudBridgeProvider.saveThought(it.agentName, it.domain, it.message)
    }
    
    application {
    Window(onCloseRequest = ::exitApplication, title = "THE 100TRAL - Command Center") {
        THE100TRALTheme {
            val secretStore = remember { SecureSecretStore() }
            val llmService = remember { LLMService() }
            val memoryStorage = remember { MemoryStorage() }
            val server = remember { MainServer(com.example.the100tral.platform.orchestration.SuperOrchestrator(llmService, memoryStorage)) }
            val scope = rememberCoroutineScope()

            MainContainer(
                onSendCommand = { command, _ ->
                    scope.launch {
                        server.handleUserRequest(request = command, domain = "GLOBAL")
                    }
                }
            ) {
                // Navigation ou autre action spécifique au Desktop
            }
        }
    }
}
