package com.example.the100tral.core.persistence

import java.io.File

actual class MemoryStorage actual constructor() {
    private val memoryFile = File(platformDataDir, "platform_knowledge_base.txt")
    private val memory = PlatformMemory()

    init {
        loadMemory()
    }

    private fun loadMemory() {
        if (memoryFile.exists()) {
            val lines = memoryFile.readLines()
            lines.forEach { line ->
                val parts = line.split("|")
                if (parts.size >= 3) {
                    try {
                        val timestamp = parts[2].toLongOrNull() ?: System.currentTimeMillis()
                        val entry = MemoryEntry(
                            domain = parts[0],
                            content = parts[1],
                            timestamp = timestamp,
                            source = if (parts.size > 3) parts[3] else "LEGACY"
                        )
                        memory.entries.add(entry)
                    } catch (e: Exception) {
                        // Skip malformed lines
                    }
                }
            }
        }
    }

    actual fun saveKnowledge(entry: MemoryEntry) {
        memory.entries.add(entry)
        try {
            memoryFile.appendText("${entry.domain}|${entry.content}|${entry.timestamp}|${entry.source}\n")
        } catch (e: Exception) {
            println("ERREUR Disque : Sauvegarde locale impossible, recours exclusif au Cloud.")
        }
        
        // Automatisation Firebase (Ordonné)
        CloudBridgeProvider.saveThought(
            agentName = "MEMORY_SYSTEM",
            domain = entry.domain,
            message = "ENREGISTREMENT MÉMOIRE : ${entry.content} [Source: ${entry.source}]"
        )
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
