package com.example.the100tral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.monitor.ThoughtMonitor

@Composable
fun ChatbotScreen(onSendMessage: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    var showKeyDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // En-tête avec bouton paramètres
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Assistant THE 100TRAL", color = Color.White, style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { showKeyDialog = true }) {
                Icon(Icons.Default.Settings, contentDescription = "Clé API", tint = Color.White)
            }
        }

        // Zone des messages (utilise les pensées des agents comme flux de chat)
        LazyColumn(
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(thoughts) { thought ->
                ChatBubble(thought)
            }
        }

        // Barre de saisie
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tapez votre commande ici...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(thought: ThoughtMonitor.AgentThought) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (thought.agentName == "User") Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (thought.agentName == "User") MaterialTheme.colorScheme.secondaryContainer else Color.White,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(thought.agentName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text(thought.message, fontSize = 15.sp)
                Text(thought.timestamp, style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}
