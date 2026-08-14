package com.example.the100tral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainContainer(
    onSendCommand: (String, String) -> Unit,
    onNavigateToHierarchy: () -> Unit,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Accueil", "Équipe", "Réglages")
    val icons = listOf(Icons.Default.Home, Icons.Default.Person, Icons.Default.Settings)

    // Détection simplifiée pour Multiplatform (peut être améliorée via BoxWithConstraints)
    BoxWithConstraints {
        val isWideScreen = this.maxWidth > 600.dp

        if (isWideScreen) {
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    containerColor = Color.White,
                    header = {
                        Text("100TRAL", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 16.dp))
                    }
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationRailItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
                
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF0F2F5))) {
                    ScreenContent(selectedItem, onSendCommand)
                }
            }
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = { Icon(icons[index], contentDescription = item) },
                                label = { Text(item, style = MaterialTheme.typography.labelSmall) },
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF0F2F5))) {
                    ScreenContent(selectedItem, onSendCommand)
                }
            }
        }
    }
}

@Composable
fun ScreenContent(selectedItem: Int, onSendCommand: (String, String) -> Unit) {
    when (selectedItem) {
        0 -> OrchestratorVisualizerScreen(onSendMessage = { onSendCommand(it, "LOCAL_1") })
        1 -> TeamScreen(onSendCommand = onSendCommand)
        2 -> ApiKeysScreen()
    }
}
