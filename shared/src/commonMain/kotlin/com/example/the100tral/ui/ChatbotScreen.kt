package com.example.the100tral.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.ui.components.AgentDetailDialog
import com.example.the100tral.core.contract.AgentThought

/**
 * Cette page est le JOURNAL D'ACTIVITÃ‰ complet.
 * Elle affiche tous les flux (logs + dialogue).
 */
@Composable
fun ChatbotScreen() {
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    var selectedThought by remember { mutableStateOf<AgentThought?>(null) }
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Journal d'ActivitÃ© SystÃ¨me", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(), 
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(thoughts) { thought ->
                    Surface(
                        onClick = { selectedThought = thought },
                        shape = MaterialTheme.shapes.small,
                        tonalElevation = 1.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        ThoughtItem(thought)
                    }
                }
            }

            // BARRE DE DÃ‰FILEMENT DESKTOP
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = listState)
            )
        }
    }

    selectedThought?.let { t ->
        AgentDetailDialog(thought = t, onDismiss = { selectedThought = null })
    }
}

