package com.example.the100tral.core.persistence

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.runBlocking

actual class MemoryStorage actual constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("platform_memory")
    private val memory = PlatformMemory()

    init {
        // Initial load (optional, could be reactive with snapshots)
        loadMemorySync()
    }

    private fun loadMemorySync() = runBlocking {
        try {
            val snapshot = collection.orderBy("timestamp", Query.Direction.DESCENDING).get().await()
            val entries = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MemoryEntry::class.java)
            }
            memory.entries.clear()
            memory.entries.addAll(entries)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun saveKnowledge(entry: MemoryEntry) {
        memory.entries.add(0, entry)
        val documentId = "${entry.timestamp}_${entry.domain}"
        collection.document(documentId).set(entry)
    }

    actual fun searchKnowledge(query: String, domain: String?): List<MemoryEntry> {
        return memory.entries.filter { 
            (it.content.contains(query, ignoreCase = true) || it.domain.contains(query, ignoreCase = true)) &&
            (domain == null || it.domain == domain)
        }
    }

    actual fun getHistory(topic: String): List<MemoryEntry> {
        return memory.entries
            .filter { it.content.contains(topic, ignoreCase = true) }
            .sortedBy { it.timestamp }
    }

    actual fun getAllKnowledge(): List<MemoryEntry> = memory.entries
}
