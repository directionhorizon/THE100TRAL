package com.example.the100tral.core.persistence

/**
 * Le Pont Cloud : Indexe les fichiers locaux et assure leur migration.
 * Corrigé pour fonctionner aussi bien sur PC que dans le Cloud.
 */
object CloudBridge {

    data class MigrationTask(
        val id: String,
        val localPath: String,
        val targetCollection: String,
        val description: String
    )

    // Utilisation de chemins relatifs pour être compatible avec GitHub Actions (Linux)
    private val indexRegistry = listOf(
        MigrationTask(
            id = "PROJECT_CORE",
            localPath = "./shared", 
            targetCollection = "project_structure",
            description = "Code source partagé"
        ),
        MigrationTask(
            id = "AGENT_MEMORY",
            localPath = "./platform_knowledge_base.txt",
            targetCollection = "platform_memory",
            description = "Base de connaissance"
        )
    )

    fun getRegistry() = indexRegistry
}

expect object CloudBridgeProvider {
    fun migrateAll()
    fun saveThought(agentName: String, domain: String, message: String)
}

