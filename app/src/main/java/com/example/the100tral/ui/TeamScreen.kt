package com.example.the100tral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.security.SecureSecretStore

data class ToolItem(val name: String, val icon: ImageVector, val url: String, val category: String, val tokenKey: String? = null)

@Composable
fun TeamScreen(onSendCommand: (String, String) -> Unit) {
    val uriHandler = LocalUriHandler.current
    val secretStore = remember { SecureSecretStore() }
    
    val tools = listOf(
        ToolItem("LinkedIn", Icons.Default.AccountBox, "https://www.linkedin.com", "SOCIAL", "LINKEDIN_TOKEN"),
        ToolItem("WhatsApp", Icons.Default.Call, "https://web.whatsapp.com", "COMM", "WHATSAPP_TOKEN"),
        ToolItem("TikTok", Icons.Default.PlayArrow, "https://www.tiktok.com", "SOCIAL", "TIKTOK_TOKEN"),
        ToolItem("Instagram", Icons.Default.AccountCircle, "https://www.instagram.com", "SOCIAL", "INSTAGRAM_TOKEN"),
        ToolItem("Notion", Icons.Default.List, "https://www.notion.so", "PRODUCTIVITY", "NOTION_API_KEY"),
        ToolItem("Calendar", Icons.Default.DateRange, "https://calendar.google.com", "PRODUCTIVITY", "CALENDAR_ENABLED"),
        ToolItem("ArXiv", Icons.Default.Info, "https://arxiv.org", "RESEARCH", "ACADEMIC_ENABLED"),
        ToolItem("Tavily", Icons.Default.Search, "https://tavily.com", "RESEARCH", "TAVILY_API_KEY")
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        val screenWidth = maxWidth
        val isWide = screenWidth > 600.dp
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Équipe Opérationnelle", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text("Interdépendances et Flux Externes", color = Color.Gray)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ToolSection("Communication & Social", tools.filter { it.category == "SOCIAL" || it.category == "COMM" }, secretStore, uriHandler)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ToolSection("Productivité & Data", tools.filter { it.category == "PRODUCTIVITY" }, secretStore, uriHandler)

            Spacer(modifier = Modifier.height(24.dp))

            ToolSection("Recherche & Intelligence", tools.filter { it.category == "RESEARCH" }, secretStore, uriHandler)

            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Effectifs & Disponibilité", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text("État opérationnel des agents de production", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            
            val teamMembers = listOf(
                "Super-Orchestrateur" to "Actif",
                "Chef de Projet" to "Actif",
                "Secrétaire Exécutive" to "Actif",
                "Arbitre de Crise" to "Actif",
                "Agent Produit" to "En veille",
                "Agent Marketing" to "En veille",
                "Agent Commercial" to "Actif",
                "Agent Finance" to "En veille",
                "Agent Culturel" to "En veille",
                "Agent Académique" to "En veille",
                "Studio Visuel" to "Actif"
            )
            
            val cols = if (isWide) 3 else 1
            
            Column {
                teamMembers.chunked(cols).forEach { rowMembers ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowMembers.forEach { (name, status) ->
                            Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                TeamMemberCard(name, status)
                            }
                        }
                        // Fill empty spots
                        repeat(cols - rowMembers.size) {
                            Spacer(modifier = Modifier.weight(1f).padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamMemberCard(name: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(8.dp).clip(CircleShape).background(if (status == "Actif") Color(0xFF4CAF50) else Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(status, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun ToolSection(title: String, items: List<ToolItem>, secretStore: SecureSecretStore, uriHandler: androidx.compose.ui.platform.UriHandler) {
    Column {
        Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 12.dp))
        
        items.chunked(4).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                rowItems.forEach { item ->
                    val isConnected = item.tokenKey?.let { secretStore.getSecret(it) != null } ?: true
                    SocialIconItem(item.name, item.icon, isConnected) {
                        uriHandler.openUri(item.url)
                    }
                }
                repeat(4 - rowItems.size) { Spacer(modifier = Modifier.size(60.dp)) }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SocialIconItem(name: String, icon: ImageVector, isConnected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onClick() },
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon, 
                        contentDescription = name, 
                        modifier = Modifier.size(28.dp),
                        tint = if (isConnected) MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                }
            }
            Surface(
                modifier = Modifier.size(14.dp).offset(x = 4.dp, y = 4.dp),
                shape = CircleShape,
                color = if (isConnected) Color(0xFF4CAF50) else Color(0xFFF44336),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
            ) {}
        }
        Text(
            text = name, 
            style = MaterialTheme.typography.labelSmall, 
            modifier = Modifier.padding(top = 6.dp),
            fontWeight = FontWeight.Medium,
            color = if (isConnected) Color.Black else Color.Gray
        )
    }
}


