package com.example.the100tral.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.monitor.ThoughtMonitor
import kotlin.math.sin

data class AgentNode(val id: String, val name: String, val x: Float, val y: Float, val color: Color)

@Composable
fun OrchestratorVisualizerScreen(onSendMessage: (String) -> Unit) {
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    val summary by ThoughtMonitor.lastResultSummary.collectAsState()
    
    val carAnim = remember { Animatable(0f) }
    var currentPos by remember { mutableStateOf(Offset(100f, 100f)) }
    var lastPos by remember { mutableStateOf(Offset(100f, 100f)) }

    val nodes = remember {
        listOf(
            AgentNode("ORCH", "Orch", 0.1f, 0.5f, Color(0xFF2196F3)),
            AgentNode("PM", "PM", 0.25f, 0.5f, Color(0xFFFFC107)),
            AgentNode("MKT", "Mkt", 0.45f, 0.3f, Color(0xFF4CAF50)),
            AgentNode("SLS", "Sls", 0.45f, 0.7f, Color(0xFFE91E63)),
            AgentNode("PRD", "Prd", 0.65f, 0.3f, Color(0xFF9C27B0)),
            AgentNode("FIN", "Fin", 0.65f, 0.7f, Color(0xFFFF5722)),
            AgentNode("ACA", "Aca", 0.85f, 0.5f, Color(0xFF607D8B))
        )
    }

    LaunchedEffect(thoughts) {
        if (thoughts.isNotEmpty()) {
            val latest = thoughts.first()
            val target = nodes.find { latest.agentName.contains(it.name, ignoreCase = true) || latest.domain.contains(it.id) }
            if (target != null) {
                lastPos = currentPos
                currentPos = Offset(target.x, target.y) // On stocke les ratios
                carAnim.snapTo(0f)
                carAnim.animateTo(1f, tween(1200, easing = FastOutSlowInEasing))
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0E12))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. FOND : GRILLE CYBER / CARTE STYLISÃ‰E
            for (i in 0..20) {
                for (j in 0..10) {
                    drawCircle(Color.Cyan.copy(alpha = 0.05f), radius = 2f, center = Offset(i * w / 20, j * h / 10))
                }
            }

            // 2. COURBES ÉLÉGANTES (Hiérarchie)
            nodes.forEach { node ->
                val start = Offset(nodes[1].x * w, nodes[1].y * h) // Tout part du PM
                val end = Offset(node.x * w, node.y * h)
                if (node.id != "PM" && node.id != "ORCH") {
                    val path = Path().apply {
                        moveTo(start.x, start.y)
                        quadraticTo((start.x + end.x) / 2, (start.y + end.y) / 2 - 50f, end.x, end.y)
                    }
                    drawPath(path, Color.Gray.copy(alpha = 0.2f), style = Stroke(width = 1f))
                }
            }

            // 3. LE FLUX (Point de données animé)
            val realStart = Offset(lastPos.x * w, lastPos.y * h)
            val realEnd = Offset(currentPos.x * w, currentPos.y * h)
            val midX = (realStart.x + realEnd.x) / 2
            val midY = (realStart.y + realEnd.y) / 2 - 100f
            
            val t = carAnim.value
            val animX = (1-t)*(1-t)*realStart.x + 2*(1-t)*t*midX + t*t*realEnd.x
            val animY = (1-t)*(1-t)*realStart.y + 2*(1-t)*t*midY + t*t*realEnd.y

            drawCircle(Color.White, radius = 8f, center = Offset(animX, animY))
            drawCircle(Color.Cyan.copy(alpha = 0.4f), radius = 15f * t, center = Offset(animX, animY))

            // 4. NOEUDS (Agents)
            nodes.forEach { node ->
                val pos = Offset(node.x * w, node.y * h)
                val isActive = currentPos == Offset(node.x, node.y)
                drawCircle(node.color, radius = if (isActive) 8f * pulse else 6f, center = pos)
                if (isActive) drawCircle(node.color.copy(alpha = 0.3f), radius = 20f * pulse, center = pos)
            }
        }
        
        Text(
            text = "SYSTÈME ACTIF : " + summary,
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
            color = Color.Cyan.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
