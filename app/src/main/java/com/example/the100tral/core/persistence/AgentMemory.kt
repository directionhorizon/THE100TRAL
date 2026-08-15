package com.example.the100tral.core.persistence

/**
 * Représente un fragment de connaissance capitalisé par un agent.
 */
data class MemoryEntry(
    val domain: String,
    val content: String,
    val source: String = "INTERNAL",
    val tags: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Conteneur global pour la mémoire persistante de la plateforme.
 */
data class PlatformMemory(
    val entries: MutableList<MemoryEntry> = mutableListOf()
)


