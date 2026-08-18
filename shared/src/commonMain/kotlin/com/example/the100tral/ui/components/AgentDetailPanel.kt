package com.example.the100tral.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.ui.ThoughtItem

@Composable
fun AgentDetailPanel(agentName: String, onDismiss: () -> Unit) {
    val allThoughts by ThoughtMonitor.thoughts.collectAsState()
    val agentThoughts = allThoughts.filter { it.agentName == agentName }

    Surface(
        modifier = Modifier.fillMaxWidth().height(400.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Journal de : " + agentName, style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = onDismiss) { Text("Fermer") }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (agentThoughts.isEmpty()) {
                Text("Aucune pensÃ©e.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(agentThoughts) { thought ->
                        ThoughtItem(thought)
                    }
                }
            }
        }
    }
}

