package com.example.the100tral.core.network

/**
 * Service pour récupérer des données du monde réel (GitHub, News).
 */
class ExternalIntelligenceService {

    suspend fun fetchGitHubTrends(): List<String> {
        return listOf("Kotlin 2.1", "Compose Multiplatform", "AI Agents")
    }

    suspend fun fetchMarketTrends(domain: String): String {
        return "Tendances actuelles pour $domain : Croissance de l'automatisation IA."
    }
}


