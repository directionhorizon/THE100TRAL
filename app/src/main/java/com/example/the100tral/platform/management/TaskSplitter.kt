package com.example.the100tral.platform.management

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.Command
import kotlinx.serialization.json.*

/**
 * Composant chargé de diviser une mission complexe en plusieurs commandes départementales.
 */
class TaskSplitter(private val llmService: LLMService) {

    /**
     * Analyse l'objectif et retourne une liste de Commandes en tenant compte de l'expérience passée.
     */
    suspend fun splitTask(goal: String, availableDomains: List<String>, memoryStorage: com.example.the100tral.core.persistence.MemoryStorage?): List<Command> {
        val optimizationNotes = memoryStorage?.searchKnowledge("", "MANAGEMENT_OPTIMIZATION") 
            ?.joinToString("\n") { "- ${it.content}" } ?: "Aucune optimisation antérieure."

        val prompt = """
            Tu es le Splitter de tâches de THE 100TRAL.
            Ton rôle est de diviser un objectif utilisateur complexe en plusieurs sous-tâches.
            
            Objectif : $goal
            Départements disponibles : ${availableDomains.joinToString(", ")}
            
            EXPÉRIENCE PASSÉE (Optimisation) :
            $optimizationNotes
            
            Prends en compte cette expérience pour formuler des instructions plus précises.
            RETOURNE UNIQUEMENT un tableau JSON au format :
            [
              {"domain": "NOM_DOMAINE", "instruction": "ce que le département doit faire"},
              ...
            ]
            Si l'objectif ne concerne qu'un seul département, retourne quand même un tableau avec un seul objet.
        """.trimIndent()

        val response = llmService.think(prompt)
        val commands = mutableListOf<Command>()

        try {
            // Nettoyage de la réponse LLM pour ne garder que le JSON
            val jsonStart = response.indexOf("[")
            val jsonEnd = response.lastIndexOf("]") + 1
            if (jsonStart != -1 && jsonEnd != -1) {
                val jsonStr = response.substring(jsonStart, jsonEnd)
                val jsonArray = Json.parseToJsonElement(jsonStr).jsonArray
                for (element in jsonArray) {
                    val obj = element.jsonObject
                    val domain = obj["domain"]?.jsonPrimitive?.content ?: ""
                    val instruction = obj["instruction"]?.jsonPrimitive?.content ?: ""
                    
                    if (availableDomains.contains(domain)) {
                        commands.add(Command(targetDomain = domain, instruction = instruction))
                    }
                }
            }
        } catch (e: Exception) {
            // En cas d'erreur de parsing, on renvoie une commande par défaut vers le premier domaine
            if (availableDomains.isNotEmpty()) {
                commands.add(Command(targetDomain = availableDomains[0], instruction = goal))
            }
        }

        return commands
    }
}


