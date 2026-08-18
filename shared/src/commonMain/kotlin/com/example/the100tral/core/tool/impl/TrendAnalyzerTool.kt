package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult

class TrendAnalyzerTool : ITool {
    override val toolName: String = "TrendAnalyzer"
    override val description: String = "Analyse les tendances actuelles."

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        return ToolResult(true, "Tendances: Forte croissance IA et Automation.")
    }
}

