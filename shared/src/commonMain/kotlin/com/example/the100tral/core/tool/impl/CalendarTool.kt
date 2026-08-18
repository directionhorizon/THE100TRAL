package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult

class CalendarTool : ITool {
    override val toolName: String = "CalendarManager"
    override val description: String = "GÃ¨re l'agenda."
    override suspend fun execute(params: Map<String, Any>): ToolResult {
        return ToolResult(true, "Action effectuÃ©e")
    }
}

