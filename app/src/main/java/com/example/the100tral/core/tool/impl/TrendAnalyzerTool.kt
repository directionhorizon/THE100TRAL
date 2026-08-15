package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import java.text.SimpleDateFormat
import java.util.*

/**
 * Outil permettant d'analyser l'évolution d'un sujet dans la mémoire de l'entreprise.
 */
class TrendAnalyzerTool(private val memoryStorage: MemoryStorage) : ITool {
    override val toolName: String = "TrendAnalyzer"
    override val description: String = "Analyse l'évolution d'un sujet dans le temps. Paramètre: topic"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val topic = params["topic"]?.toString() ?: return ToolResult(success = false, output = "Sujet 'topic' manquant.")
        
        val history = memoryStorage.getHistory(topic)
        
        if (history.isEmpty()) {
            return ToolResult(success = true, output = "Aucun historique trouvé pour le sujet : $topic")
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val report = StringBuilder("Rapport de tendance pour '$topic' :\n")
        
        history.forEach { entry ->
            val dateStr = dateFormat.format(Date(entry.timestamp))
            report.append("- [$dateStr] Source: ${entry.source} | Info: ${entry.content}\n")
        }

        return ToolResult(success = true, output = report.toString(), data = mapOf("history_size" to history.size))
    }
}


