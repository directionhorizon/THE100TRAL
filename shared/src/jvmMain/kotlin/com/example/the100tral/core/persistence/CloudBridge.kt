package com.example.the100tral.core.persistence

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.serialization.json.*

actual object CloudBridgeProvider {
    private val client = HttpClient()
    private val projectId = "com-example-the100tral-66bff"

    actual fun migrateAll() {
        println("CloudBridge: Lancement de la migration vers Firebase Firestore...")
        
        runBlocking {
            CloudBridge.getRegistry().forEach { task ->
                val file = File(task.localPath)
                if (file.exists()) {
                    if (file.isDirectory) {
                        file.walkTopDown().forEach { subFile ->
                            if (subFile.isFile) uploadFileToFirestore(subFile, task.targetCollection)
                        }
                    } else {
                        uploadFileToFirestore(file, task.targetCollection)
                    }
                }
            }
        }
        println("CloudBridge: Migration terminée !")
    }

    /**
     * Automatisation de la sauvegarde des pensées et actions dans Firebase (Ordonné).
     */
    actual fun saveThought(agentName: String, domain: String, message: String) {
        GlobalScope.launch {
            try {
                val timestamp = System.currentTimeMillis()
                val documentId = "${timestamp}_${agentName.replace(" ", "_")}"
                val url = "https://firestore.googleapis.com/v1/projects/$projectId/databases/(default)/documents/functional_activity/$documentId"
                
                client.patch(url) {
                    contentType(ContentType.Application.Json)
                    setBody(buildJsonObject {
                        putJsonObject("fields") {
                            putJsonObject("agentName") { put("stringValue", agentName) }
                            putJsonObject("domain") { put("stringValue", domain) }
                            putJsonObject("message") { put("stringValue", message) }
                            putJsonObject("timestamp") { put("doubleValue", timestamp.toDouble()) }
                        }
                    }.toString())
                }
            } catch (e: Exception) {
                // Silencieusement ignoré pour ne pas bloquer l'agent
            }
        }
    }

    private suspend fun uploadFileToFirestore(file: File, collection: String) {
        try {
            val content = file.readText()
            val documentId = file.name.replace(".", "_").replace(" ", "_")
            val url = "https://firestore.googleapis.com/v1/projects/$projectId/databases/(default)/documents/$collection/$documentId"
            
            val response = client.patch(url) {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    putJsonObject("fields") {
                        putJsonObject("name") { put("stringValue", file.name) }
                        putJsonObject("content") { put("stringValue", content) }
                        putJsonObject("path") { put("stringValue", file.absolutePath) }
                        putJsonObject("timestamp") { put("doubleValue", System.currentTimeMillis().toDouble()) }
                    }
                }.toString())
            }
            
            if (response.status.isSuccess()) {
                println("✔ Migré : ${file.name}")
            }
        } catch (e: Exception) {
            println("✘ Erreur migration ${file.name}: ${e.message}")
        }
    }
}
