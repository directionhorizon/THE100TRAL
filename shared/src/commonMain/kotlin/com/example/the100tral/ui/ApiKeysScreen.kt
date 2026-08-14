package com.example.the100tral.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the100tral.core.security.SecureSecretStore
import com.example.the100tral.core.security.AuthWrapper

@Composable
fun ApiKeysScreen() {
    val secretStore = remember { SecureSecretStore() }
    val effectiveEmail = AuthWrapper.getCurrentUserEmail()
    val registeredEmail = secretStore.getSecret("OWNER_EMAIL")
    
    if (effectiveEmail == null) {
        GoogleSignInScreen { email ->
            AuthWrapper.signInSimulation(email)
            if (registeredEmail == null) {
                secretStore.saveSecret("OWNER_EMAIL", email)
            }
        }
    } else if (registeredEmail != null && effectiveEmail != registeredEmail) {
        AccessDeniedScreen(effectiveEmail)
    } else {
        SettingsContent(secretStore, effectiveEmail)
    }
}

@Composable
fun GoogleSignInScreen(onSignIn: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Text("Authentification Unique", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Connectez-vous pour déverrouiller vos clés API.", textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onSignIn("user@gmail.com") }, modifier = Modifier.fillMaxWidth()) { 
            Text("Se connecter") 
        }
    }
}

@Composable
fun AccessDeniedScreen(email: String) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Red)
        Text("Accès Refusé", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Le compte $email n'est pas autorisé.", textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = Color.Gray)
    }
}

@Composable
fun SettingsContent(secretStore: SecureSecretStore, email: String) {
    var geminiKey by remember { mutableStateOf(secretStore.getSecret("GEMINI_API_KEY") ?: "") }
    var notionKey by remember { mutableStateOf(secretStore.getSecret("NOTION_API_KEY") ?: "") }
    var tavilyKey by remember { mutableStateOf(secretStore.getSecret("TAVILY_API_KEY") ?: "") }
    
    var showAddKeyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Paramètres Système", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Text("Connecté en tant que : $email", color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.Lock, contentDescription = "Sécurisé", tint = Color(0xFF4CAF50))
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        SettingsCard(title = "Clés API d'Intelligence") {
            ApiKeyField("Gemini Flash", geminiKey) { geminiKey = it; secretStore.saveSecret("GEMINI_API_KEY", it) }
            ApiKeyField("Notion Integrator", notionKey) { notionKey = it; secretStore.saveSecret("NOTION_API_KEY", it) }
            ApiKeyField("Tavily Search", tavilyKey) { tavilyKey = it; secretStore.saveSecret("TAVILY_API_KEY", it) }
            
            Button(
                onClick = { showAddKeyDialog = true },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ajouter une clé")
            }
        }
    }

    if (showAddKeyDialog) {
        AddKeyDialog(onDismiss = { showAddKeyDialog = false }) { name, value ->
            secretStore.saveSecret(name.uppercase(), value)
            showAddKeyDialog = false
        }
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 12.dp))
            content()
        }
    }
}

@Composable
fun AddKeyDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouvelle Clé API") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nom de la clé") })
                OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("Valeur") })
            }
        },
        confirmButton = { Button(onClick = { onAdd(name, value) }) { Text("Ajouter") } }
    )
}

@Composable
fun ApiKeyField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true
    )
}
