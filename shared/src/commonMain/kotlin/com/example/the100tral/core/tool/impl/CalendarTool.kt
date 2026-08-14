package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import java.text.SimpleDateFormat
import java.util.*

/**
 * Outil de gestion d'agenda et de rappels.
 */
class CalendarTool : ITool {
    override val toolName: String = "CalendarManager"
    override val description: String = "Gère l'agenda. Paramètres: action (create/list), title, date (dd/MM/yyyy HH:mm)"

    private val events = mutableListOf<String>()

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val action = params["action"]?.toString() ?: "list"
        val title = params["title"]?.toString() ?: "Sans titre"
        val dateStr = params["date"]?.toString() ?: ""

        return when (action) {
            "create" -> {
                val event = "$dateStr : $title"
                events.add(event)
                ThoughtMonitor.publish("Assistant", "CALENDAR", "Rappel créé : $event")
                ToolResult(success = true, output = "Événement créé avec succès : $event")
            }
            "list" -> {
                val list = if (events.isEmpty()) "Aucun événement." else events.joinToString("\n")
                ToolResult(success = true, output = "Liste des événements :\n$list")
            }
            else -> ToolResult(success = false, output = "Action inconnue.")
        }
    }
}
