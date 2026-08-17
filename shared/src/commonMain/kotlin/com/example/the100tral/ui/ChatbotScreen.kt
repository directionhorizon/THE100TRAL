package com.example.the100tral.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.contract.AgentThought

@Composable
fun ChatbotScreen() {
    // Utilisation directe de la liste (on simplifiera en StateFlow plus tard pour la rÃ©activitÃ©)
    val thoughts = ThoughtMonitor.thoughts 

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Thought Monitor", style = MaterialTheme.typography.headlineMedium)
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(thoughts) { thought ->
                ThoughtItem(thought)
            }
        }
    }
}

@Composable
fun ThoughtItem(thought: AgentThought) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("${thought.agentName} (${thought.domain})", style = MaterialTheme.typography.labelLarge)
            Text(thought.message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

