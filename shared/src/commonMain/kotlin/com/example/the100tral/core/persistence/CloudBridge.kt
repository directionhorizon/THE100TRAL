package com.example.the100tral.core.persistence

/**
 * Le Pont Cloud : Indexe les fichiers locaux critiques et assure leur migration vers Firebase.
 */
object CloudBridge {

    data class MigrationTask(
        val id: String,
        val localPath: String,
        val targetCollection: String,
        val description: String
    )

    // Liste des fichiers "Bons pour indexation" identifiés sur D:\android studio
    private val indexRegistry = listOf(
        MigrationTask(
            id = "PROJECT_CORE",
            localPath = "D:/android studio/AndroidStudioProjects/THE100TRAL/shared",
            targetCollection = "project_structure",
            description = "Code source partagé (Logique des agents)"
        ),
        MigrationTask(
            id = "FUNCTIONAL_HISTORY",
            localPath = "D:/android studio/studio/agent/conversations",
            targetCollection = "functional_activity",
            description = "Historique des interactions et décisions de conception"
        ),
        MigrationTask(
            id = "AGENT_MEMORY",
            localPath = "D:/android studio/AndroidStudioProjects/THE100TRAL/platform_knowledge_base.txt",
            targetCollection = "platform_memory",
            description = "Base de connaissance capitalisée"
        )
    )

    fun getRegistry() = indexRegistry
}

expect object CloudBridgeProvider {
    fun migrateAll()
    fun saveThought(agentName: String, domain: String, message: String)
}
