package com.example.the100tral.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.monitor.ThoughtMonitor

@Composable
fun AgentDetailPanel(agentName: String, onDismiss: () -> Unit) {
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    val agentThoughts = thoughts.filter { it.agentName == agentName }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Journal : $agentName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onDismiss) {
                    Text("Fermer")
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            if (agentThoughts.isEmpty()) {
                Text("Aucune activité enregistrée.", color = Color.Gray, fontSize = 14.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(agentThoughts) { thought ->
                        Column {
                            Text(thought.timestamp, fontSize = 10.sp, color = Color.Gray)
                            Text(thought.message, fontSize = 14.sp, color = Color.Black)
                            Divider(modifier = Modifier.padding(top = 4.dp), color = Color(0xFFEEEEEE))
                        }
                    }
                }
            }
        }
    }
}

