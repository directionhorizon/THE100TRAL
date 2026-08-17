package com.example.the100tral.core.persistence

expect class MemoryStorage() {
    fun saveKnowledge(entry: MemoryEntry)
    fun searchKnowledge(query: String, domain: String? = null): List<MemoryEntry>
    fun getHistory(topic: String): List<MemoryEntry>
    fun getAllKnowledge(): List<MemoryEntry>
}

