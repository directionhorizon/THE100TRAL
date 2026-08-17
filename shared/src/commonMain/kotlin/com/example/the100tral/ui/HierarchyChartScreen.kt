package com.example.the100tral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.monitor.ThoughtMonitor

import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.the100tral.ui.components.AgentDetailPanel

@Composable
fun HierarchyChartScreen() {
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    val activeAgents = thoughts.take(5).map { it.agentName }.toSet()
    
    var selectedAgent by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ORGANIGRAMME DYNAMIQUE", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))
            NodeBox("Super-Orchestrateur", "N1", activeAgents.contains("Super-Orchestrateur")) {
                selectedAgent = "Super-Orchestrateur"
            }
            Spacer(Modifier.height(10.dp))
            NodeBox("Chef de Projet", "N2", activeAgents.contains("Chef de Projet")) {
                selectedAgent = "Chef de Projet"
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                NodeBox("Produit", "N3", activeAgents.contains("Département Produit & Développement"), isSmall = true) {
                    selectedAgent = "Département Produit & Développement"
                }
                NodeBox("Marketing", "N3", activeAgents.contains("Département Marketing Multi-canal"), isSmall = true) {
                    selectedAgent = "Département Marketing Multi-canal"
                }
                NodeBox("Finance", "N3", activeAgents.contains("Département Financier"), isSmall = true) {
                    selectedAgent = "Département Financier"
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Astuce : Cliquez sur un agent pour voir son journal.", color = Color.Gray, fontSize = 10.sp)
        }

        // Overlay Detail Panel
        if (selectedAgent != null) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))
                    .clickable { selectedAgent = null },
                contentAlignment = Alignment.BottomCenter
            ) {
                AgentDetailPanel(selectedAgent!!) {
                    selectedAgent = null
                }
            }
        }
    }
}

@Composable
fun NodeBox(name: String, level: String, isActive: Boolean, isSmall: Boolean = false, onClick: () -> Unit) {
    val color = if (isActive) Color.Green else Color.Gray
    Box(
        modifier = Modifier
            .size(if (isSmall) 85.dp else 130.dp, 65.dp)
            .border(1.dp, color, RoundedCornerShape(8.dp))
            .background(Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(level, color = color, fontSize = 10.sp)
            Text(name.take(12), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}


