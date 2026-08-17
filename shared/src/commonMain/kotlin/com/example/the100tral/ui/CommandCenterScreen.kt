package com.example.the100tral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.monitor.ThoughtMonitor
import kotlinx.coroutines.launch

@Composable
fun CommandCenterScreen(onSendCommand: (String, String) -> Unit) {
    var commandText by remember { mutableStateOf("") }
    var providerKey by remember { mutableStateOf("LOCAL_1") }
    val thoughts by ThoughtMonitor.thoughts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        // Zone de saisie
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = commandText,
                    onValueChange = { commandText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Quelle est votre mission ?") },
                    placeholder = { Text("Ex: Crée un site web et fais la promotion...") }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Moteur IA : ", fontWeight = FontWeight.Medium)
                    
                    FilterChip(
                        selected = providerKey == "LOCAL_1",
                        onClick = { providerKey = "LOCAL_1" },
                        label = { Text("Local (Llama)") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = providerKey == "GEMINI",
                        onClick = { providerKey = "GEMINI" },
                        label = { Text("Gemini") }
                    )
                }

                Button(
                    onClick = {
                        if (commandText.isNotBlank()) {
                            onSendCommand(commandText, providerKey)
                            commandText = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lancer")
                }
            }
        }

        // Flux de pensées en temps réel
        Text(
            text = "Journal d'activité",
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // On affiche les 10 dernières pensées pour ne pas surcharger la page Equipe
        thoughts.take(10).forEach { thought ->
            ThoughtItem(thought)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Preview removed for commonMain compatibility or use Multiplatform Preview if configured

