package com.example.the100tral.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.the100tral.core.contract.AgentData
import com.example.the100tral.core.EmpireController

@Composable
fun ApiKeysScreen(agents: List<AgentData>) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState)) {
        Text("GOUVERNANCE DES INTELLIGENCES", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        agents.forEach { agent ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(agent.agentIdentifier, style = MaterialTheme.typography.titleMedium)
                            Text("Modèle : " + agent.preferredModel, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row {
                        Button(onClick = { /* Test de connexion HTTP */ }) {
                            Text("TESTER CONNEXION")
                        }
                        Spacer(Modifier.width(8.dp))
                        // BOUTON DE CHARGEMENT RÉEL
                        OutlinedButton(onClick = { EmpireController.wakeUpIA(agent.preferredModel) }) {
                            Text("LANCER VIA PINOKIO")
                        }
                    }
                }
            }
        }
    }
}
