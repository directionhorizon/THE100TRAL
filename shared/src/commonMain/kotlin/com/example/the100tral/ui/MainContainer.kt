package com.example.the100tral.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.the100tral.core.contract.AgentData

@Composable
fun MainContainer(
    onSendCommand: (String, String) -> Unit,
    onNavigateToHierarchy: () -> Unit,
    filePicker: NativeFilePicker? = null,
    agents: List<AgentData> = emptyList()
) {
    var tab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = tab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            // Noms ecrits sans accents si necessaire pour eviter les bugs Windows
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("ACCUEIL") })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("EQUIPE") })
            Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("JOURNAL") })
            Tab(selected = tab == 3, onClick = { tab = 3 }, text = { Text("REGLAGES") })
        }
        
        Box(modifier = Modifier.weight(1f)) {
            when (tab) {
                0 -> CommandCenterScreen(onSendCommand = onSendCommand, filePicker = filePicker)
                1 -> TeamScreen(agents) 
                2 -> ChatbotScreen() 
                3 -> ApiKeysScreen(agents)
            }
        }
    }
}
