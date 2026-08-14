package com.example.the100tral.core.persistence

import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

actual object CloudBridgeProvider {
    actual fun migrateAll() {
        val db = FirebaseFirestore.getInstance()
        CloudBridge.getRegistry().forEach { task ->
            val file = File(task.localPath)
            if (file.exists()) {
                if (file.isDirectory) {
                    file.listFiles()?.forEach { subFile ->
                        uploadFile(subFile, task.targetCollection, db)
                    }
                } else {
                    uploadFile(file, task.targetCollection, db)
                }
            }
        }
    }

    /**
     * Automatisation Android vers Firebase (Ordonné).
     */
    actual fun saveThought(agentName: String, domain: String, message: String) {
        val db = FirebaseFirestore.getInstance()
        val timestamp = System.currentTimeMillis()
        val data = mapOf(
            "agentName" to agentName,
            "domain" to domain,
            "message" to message,
            "timestamp" to timestamp
        )
        val documentId = "${timestamp}_${agentName.replace(" ", "_")}"
        db.collection("functional_activity").document(documentId).set(data)
    }

    private fun uploadFile(file: File, collection: String, db: FirebaseFirestore) {
        if (file.isDirectory) return 
        val data = mapOf(
            "name" to file.name,
            "content" to file.readText(),
            "timestamp" to System.currentTimeMillis(),
            "path" to file.absolutePath
        )
        db.collection(collection).document(file.name.replace(".", "_")).set(data)
    }
}
