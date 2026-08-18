package com.example.the100tral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.the100tral.core.monitor.ThoughtMonitor

import androidx.compose.foundation.*

@Composable
fun HierarchyChartScreen() {
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    val scrollState = rememberScrollState()
    
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
        Text("HIERARCHIE DES AGENTS", color = Color.White)
        Spacer(Modifier.height(20.dp))
        Box(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                // Affichage simplifié pour validation de build
                for (thought in thoughts) {
                    Text("Agent Actif: " + thought.agentName, color = Color.Green, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = scrollState)
            )
        }
    }
}

