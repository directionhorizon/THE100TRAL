package com.example.the100tral.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.contract.AgentThought

// Interface pour le Picker natif
interface NativeFilePicker {
    fun pick(): String?
}

@Composable
fun CommandCenterScreen(onSendCommand: (String, String) -> Unit, filePicker: NativeFilePicker? = null) {
    var text by remember { mutableStateOf("") }
    val allThoughts by ThoughtMonitor.thoughts.collectAsState()
    val dialogs = remember(allThoughts) { allThoughts.filter { it.isDialogue } }
    val listState = rememberLazyListState()
    
    var showSourceDialog by remember { mutableStateOf(false) }
    var attachedFilePath by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).background(Color(0xFF0F1113))) {
            OrchestratorVisualizerScreen(onSendMessage = {}) 
        }

        HorizontalDivider(thickness = 1.dp)

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(dialogs.reversed()) { thought ->
                    ChatBubble(thought = thought)
                }
            }
            VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(), adapter = rememberScrollbarAdapter(scrollState = listState))
        }

        Surface(tonalElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
            Column {
                attachedFilePath?.let { path ->
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("📎 " + path.split("\\").last(), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                            IconButton(onClick = { attachedFilePath = null }) { Text("X", color = Color.Red) }
                        }
                    }
                }
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { showSourceDialog = true }) {
                        Text("📎", style = MaterialTheme.typography.headlineSmall)
                    }
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Parler au Super-Orchestrateur...") },
                        trailingIcon = {
                            if (text.isNotBlank() || attachedFilePath != null) {
                                IconButton(onClick = { 
                                    val finalMsg = if (attachedFilePath != null) text + " [Fichier: " + attachedFilePath + "]" else text
                                    onSendCommand(finalMsg, "GLOBAL")
                                    text = ""
                                    attachedFilePath = null
                                }) {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Envoyer")
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Joindre un document") },
            text = { Text("Où se trouve votre fichier ?") },
            confirmButton = {
                Button(onClick = { 
                    showSourceDialog = false
                    attachedFilePath = filePicker?.pick()
                }) { Text("Mon PC") }
            },
            dismissButton = {
                TextButton(onClick = { showSourceDialog = false }) { Text("Google Drive") }
            }
        )
    }
}

@Composable
fun ChatBubble(thought: AgentThought) {
    val isUser = thought.agentName == "Utilisateur"
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
        Surface(
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
            shape = if (isUser) MaterialTheme.shapes.medium.copy(bottomEnd = CornerSize(0.dp)) else MaterialTheme.shapes.medium.copy(bottomStart = CornerSize(0.dp)),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(if (isUser) "Moi" else thought.agentName, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if(isUser) Color.White.copy(alpha=0.7f) else MaterialTheme.colorScheme.primary)
                Text(thought.message, style = MaterialTheme.typography.bodyLarge, color = if(isUser) Color.White else Color.Unspecified)
            }
        }
    }
}
