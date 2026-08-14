package com.example.the100tral.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.monitor.ThoughtMonitor
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

data class Building(
    val id: String,
    val name: String,
    val level: Int, // 0: Super, 1: Supervision, 2: Operational
    val color: Color,
    val tools: List<String> = emptyList(),
    val budget: String = "Optimisé",
    val memory: String = "100% Synchro",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrchestratorVisualizerScreen(onSendMessage: (String) -> Unit) {
    val thoughts by ThoughtMonitor.thoughts.collectAsState()
    val summary by ThoughtMonitor.lastResultSummary.collectAsState()
    var promptText by remember { mutableStateOf("") }
    
    val carAnim = remember { Animatable(0f) }
    var selectedBuilding by remember { mutableStateOf<Building?>(null) }
    
    // Position tracking for the car
    var lastAgentName by remember { mutableStateOf("Super-Orchestrateur") }
    var currentAgentName by remember { mutableStateOf("Super-Orchestrateur") }

    val buildings = remember {
        listOf(
            Building("SUPER_ORCH", "Super-Orchestrateur", 0, Color(0xFF00E5FF), listOf("Vision Stratégique")),
            Building("PM", "Chef de Projet", 1, Color(0xFFFFD600), listOf("Arbitrage", "Délégation")),
            Building("EA", "Secrétaire Exécutive", 1, Color(0xFFB0BEC5), listOf("Journalisation", "Support")),
            Building("CA", "Arbitre de Crise", 1, Color(0xFFFF5252), listOf("Gestion de Conflits")),
            Building("PRODUCT", "Produit", 2, Color(0xFF00FF9D), listOf("Backend", "Frontend", "DevOps")),
            Building("MARKETING", "Marketing", 2, Color(0xFFFF00D4), listOf("Social Media", "Ads", "Trends")),
            Building("COMMERCIAL", "Commercial", 2, Color(0xFF2196F3), listOf("Leads", "Customer Success")),
            Building("FINANCE", "Finance", 2, Color(0xFF4CAF50), listOf("Analyse ROI", "Budget")),
            Building("CULTURE", "Culture", 2, Color(0xFF9C27B0), listOf("Intelligence Culturelle")),
            Building("ACADEMIC", "Académique", 2, Color(0xFFFF9800), listOf("Recherche", "Tavily")),
            Building("VISUAL_STUDIO", "Studio Visuel", 2, Color(0xFFFF5722), listOf("Logo", "Script Vidéo", "Assets"))
        )
    }

    LaunchedEffect(thoughts) {
        if (thoughts.isNotEmpty()) {
            val latest = thoughts.first()
            lastAgentName = currentAgentName
            currentAgentName = latest.agentName
            
            carAnim.snapTo(0f)
            carAnim.animateTo(1f, tween(1200, easing = FastOutSlowInEasing))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5))) {
        val isWide = this.maxWidth > 800.dp
        
        if (isWide) {
            // Tablette : Map à gauche, Synthèse à droite
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                    AgentGPSMap(
                        buildings = buildings,
                        carProgress = carAnim.value,
                        fromAgent = lastAgentName,
                        toAgent = currentAgentName,
                        onBuildingClick = { selectedBuilding = it }
                    )
                    
                    // Search Bar Floating Top Left
                    SearchOverlay(
                        promptText = promptText,
                        onPromptChange = { promptText = it },
                        onSend = { 
                            onSendMessage(promptText)
                            promptText = ""
                        },
                        modifier = Modifier.width(400.dp).padding(16.dp).align(Alignment.TopStart)
                    )
                }
                
                // Sidebar Synthèse
                Surface(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    SynthesisPanel(summary, thoughts.firstOrNull()?.executionTimeMs ?: 0L, isExpanded = true)
                }
            }
        } else {
            // Téléphone : Standard
            MatrixGridBackground()
            AgentGPSMap(
                buildings = buildings,
                carProgress = carAnim.value,
                fromAgent = lastAgentName,
                toAgent = currentAgentName,
                onBuildingClick = { selectedBuilding = it }
            )
            SearchOverlay(
                promptText = promptText,
                onPromptChange = { promptText = it },
                onSend = { 
                    onSendMessage(promptText)
                    promptText = ""
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.TopCenter)
            )
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp)) {
                SynthesisPanel(summary, thoughts.firstOrNull()?.executionTimeMs ?: 0L, isExpanded = false)
            }
        }

        // Technical Sheet Dialog
        if (selectedBuilding != null) {
            TechnicalSheetDialog(building = selectedBuilding!!, onDismiss = { selectedBuilding = null })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOverlay(promptText: String, onPromptChange: (String) -> Unit, onSend: () -> Unit, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = promptText,
                onValueChange = onPromptChange,
                placeholder = { Text("Mission opérationnelle...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            IconButton(onClick = onSend) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Envoyer", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SynthesisPanel(summary: String, delay: Long, isExpanded: Boolean) {
    Surface(
        color = Color.White,
        shape = if (isExpanded) RoundedCornerShape(0.dp) else RoundedCornerShape(16.dp),
        shadowElevation = if (isExpanded) 0.dp else 8.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("SYNTHÈSE DE PRODUCTION", style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold))
                Spacer(modifier = Modifier.weight(1f))
                Text("Délai : ${delay}ms", style = TextStyle(color = Color.Gray, fontSize = 10.sp))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray)
            
            val scrollState = rememberScrollState()
            Text(
                text = summary.ifBlank { "Aucune mission en cours. Système prêt." },
                color = Color.Black,
                fontSize = if (isExpanded) 16.sp else 14.sp,
                lineHeight = if (isExpanded) 24.sp else 20.sp,
                modifier = if (isExpanded) Modifier.verticalScroll(scrollState) else Modifier
            )
        }
    }
}

@Composable
fun AgentGPSMap(
    buildings: List<Building>,
    carProgress: Float,
    fromAgent: String,
    toAgent: String,
    onBuildingClick: (Building) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val buildingCoords = remember { mutableMapOf<String, Offset>() }

    Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
        detectTapGestures { offset ->
            buildingCoords.forEach { (id, coord) ->
                if (Offset(offset.x - coord.x, offset.y - coord.y).getDistance() < 40.dp.toPx()) {
                    buildings.find { b -> (b.id == id) || (b.name == id) }?.let { onBuildingClick(it) }
                }
            }
        }
    }) {
        val w = size.width
        val h = size.height

        // 1. Définir les positions
        val superPos = Offset(w * 0.5f, h * 0.2f)
        val supervisionCenter = Offset(w * 0.5f, h * 0.45f)
        
        // Sous-agents de supervision (Triangle autour du centre)
        val eaPos = Offset(supervisionCenter.x - 60.dp.toPx(), supervisionCenter.y)
        val pmPos = Offset(supervisionCenter.x, supervisionCenter.y - 40.dp.toPx())
        val caPos = Offset(supervisionCenter.x + 60.dp.toPx(), supervisionCenter.y)

        buildingCoords["SUPER_ORCH"] = superPos
        buildingCoords["PM"] = pmPos
        buildingCoords["EA"] = eaPos
        buildingCoords["CA"] = caPos

        // Agents opérationnels en cercle au fond
        val operationalCenter = Offset(w * 0.5f, h * 0.75f)
        val radius = 120.dp.toPx()
        val opBuildings = buildings.filter { it.level == 2 }
        opBuildings.forEachIndexed { index, b ->
            val angle = (index * (360f / opBuildings.size) - 90f) * (Math.PI / 180).toFloat()
            val pos = Offset(
                operationalCenter.x + radius * cos(angle.toDouble()).toFloat(),
                operationalCenter.y + radius * sin(angle.toDouble()).toFloat()
            )
            buildingCoords[b.id] = pos
        }

        // 2. Dessiner les "Routes" (Liens)
        val roadColor = Color(0xFF161B22).copy(alpha = 0.1f)
        // Super -> PM
        drawRoute(superPos, pmPos, roadColor)
        // Supervision Hub
        drawRoute(pmPos, eaPos, roadColor)
        drawRoute(pmPos, caPos, roadColor)
        // PM -> All Operational
        opBuildings.forEach { b ->
            buildingCoords[b.id]?.let { drawRoute(pmPos, it, roadColor) }
        }

        // 3. Dessiner les bâtiments
        buildings.forEach { b ->
            val pos = buildingCoords[b.id] ?: Offset.Zero
            drawBuilding(b, pos, textMeasurer)
        }

        // 4. Dessiner la "Voiture" (GPS)
        val start = buildingCoords[fromAgent] ?: buildingCoords.values.firstOrNull() ?: Offset.Zero
        val end = buildingCoords[toAgent] ?: buildingCoords.values.firstOrNull() ?: Offset.Zero
        
        if (carProgress > 0f) {
            val currentPos = Offset(
                start.x + (end.x - start.x) * carProgress,
                start.y + (end.y - start.y) * carProgress
            )
            
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFF00E5FF).copy(alpha = 0.4f), Color.Transparent), center = currentPos, radius = 20.dp.toPx()),
                radius = 20.dp.toPx(),
                center = currentPos
            )
            drawCircle(Color.White, radius = 5.dp.toPx(), center = currentPos)
        }
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRoute(from: Offset, to: Offset, color: Color) {
    drawLine(
        color = color,
        start = from,
        end = to,
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBuilding(
    building: Building, 
    pos: Offset, 
    textMeasurer: TextMeasurer
) {
    val size = when(building.level) {
        0 -> 45.dp.toPx()
        1 -> 35.dp.toPx()
        else -> 30.dp.toPx()
    }

    drawCircle(
        brush = Brush.radialGradient(listOf(building.color.copy(alpha = 0.2f), Color.Transparent), center = pos, radius = size * 1.5f),
        radius = size * 1.5f,
        center = pos
    )

    drawRoundRect(
        color = Color.White,
        topLeft = Offset(pos.x - size/2, pos.y - size/2),
        size = Size(size, size),
        cornerRadius = CornerRadius(8.dp.toPx())
    )
    
    drawRoundRect(
        color = building.color,
        topLeft = Offset(pos.x - size/2, pos.y - size/2),
        size = Size(size, size),
        style = Stroke(width = 2.dp.toPx()),
        cornerRadius = CornerRadius(8.dp.toPx())
    )

    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(building.name),
        style = TextStyle(color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(pos.x - textLayoutResult.size.width / 2, pos.y + size / 2 + 4.dp.toPx())
    )
}

@Composable
fun MatrixGridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 60.dp.toPx()
        for (x in 0..(size.width / step).toInt()) {
            drawLine(Color.Black.copy(alpha = 0.05f), Offset(x * step, 0f), Offset(x * step, size.height))
        }
        for (y in 0..(size.height / step).toInt()) {
            drawLine(Color.Black.copy(alpha = 0.05f), Offset(0f, y * step), Offset(size.width, y * step))
        }
    }
}

@Composable
fun TechnicalSheetDialog(building: Building, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fermer") } },
        title = { Text(building.name) },
        text = {
            Column {
                Text("Expertise : ${building.tools.joinToString(", ")}")
                Text("Budget : ${building.budget}")
                Text("Mémoire : ${building.memory}")
            }
        }
    )
}

// Removed redundant custom drawRoundRect extension
