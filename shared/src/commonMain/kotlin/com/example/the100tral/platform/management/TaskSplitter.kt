package com.example.the100tral.platform.management

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.Command
import com.example.the100tral.core.persistence.MemoryStorage
import kotlinx.serialization.json.*

/**
 * Composant chargaƒÂ© de diviser une mission complexe en plusieurs commandes daƒÂ©partementales.
 */
class TaskSplitter(
    private val llmService: LLMService?,
    private val memoryStorage: MemoryStorage?
) {

    /**
     * Analyse l'objectif et retourne une liste de Commandes.
     */
    suspend fun splitTask(goal: String, availableDomains: List<String>): List<Command> {
        if (llmService == null) return emptyList()
        
        val prompt = "Divise cet objectif en sous-taƒÂ¢ches JSON pour ces domaines: " + availableDomains.joinToString(", ") + "\nObjectif: " + goal
        
        val response = llmService.performAction(prompt)
        val commands = mutableListOf<Command>()

        try {
            val jsonStart = response.indexOf("[")
            val jsonEnd = response.lastIndexOf("]") + 1
            if (jsonStart != -1 && jsonEnd != -1) {
                val jsonStr = response.substring(jsonStart, jsonEnd)
                val jsonArray = Json.decodeFromString<JsonArray>(jsonStr)
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
            if (availableDomains.isNotEmpty()) {
                commands.add(Command(targetDomain = availableDomains[0], instruction = goal))
            }
        }

        return commands
    }
}


