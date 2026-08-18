package com.example.the100tral.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.contract.AgentThought

@Composable
fun AgentDetailDialog(thought: AgentThought, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) { Text("Fermer") }
        },
        title = { Text("Détails de l'activité : " + thought.agentName) },
        text = {
            Column {
                Text("Domaine : " + thought.domain, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(thought.message, style = MaterialTheme.typography.bodyLarge)
                
                if (thought.executionTimeMs > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Temps d'exécution : " + thought.executionTimeMs + "ms", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    )
}

