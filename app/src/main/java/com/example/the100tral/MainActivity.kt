package com.example.the100tral

import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.example.the100tral.core.monitor.ThoughtMonitor
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.the100tral.platform.management.*
import com.example.the100tral.platform.departments.product.ProductDevelopmentDepartment
import com.example.the100tral.platform.departments.product.sub.*
import com.example.the100tral.platform.departments.marketing.MarketingDepartment
import com.example.the100tral.platform.departments.marketing.sub.*
import com.example.the100tral.platform.departments.culture.CultureIntelligenceDepartment
import com.example.the100tral.platform.departments.finance.FinancialDepartment
import com.example.the100tral.platform.departments.commercial.CommercialDepartment
import com.example.the100tral.platform.departments.sales.sub.*
import com.example.the100tral.platform.departments.academic.AcademicAgent
import com.example.the100tral.platform.departments.visual.VisualStudioDepartment
import com.example.the100tral.platform.orchestration.SuperOrchestrator
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.ai.providers.LocalLLMProvider
import com.example.the100tral.core.persistence.CloudBridgeProvider
import com.example.the100tral.core.security.AndroidContext
import com.example.the100tral.core.security.SecureSecretStore
import com.example.the100tral.core.network.EmailService
import com.example.the100tral.core.network.TavilyService
import com.example.the100tral.core.tool.impl.*
import com.example.the100tral.server.MainServer
import com.example.the100tral.ui.MainContainer
import com.example.the100tral.ui.theme.THE100TRALTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Initialisation de Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialisation du contexte Android pour les modules partagés
        AndroidContext.context = this
        
        // Liaison du moniteur d'activité au Cloud Firebase (Automatisé et Ordonné)
        ThoughtMonitor.setPersistenceListener {
            CloudBridgeProvider.saveThought(it.agentName, it.domain, it.message)
        }

        // Lancement du Pont Cloud (Migration et Indexation)
        CloudBridgeProvider.migrateAll()
        
        // Initialisation des Services Core
        val secretStore = SecureSecretStore()
        
        // Injection des clés fournies par l'utilisateur (Sauvegarde sécurisée)
        secretStore.saveSecret("GEMINI_API_KEY", "AIzaSyBJVF0UXd2ui4XH9n3JaYZznRhc41LbuxU")
        secretStore.saveSecret("NOTION_API_KEY", "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")
        secretStore.saveSecret("TAVILY_API_KEY", "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")
        
        val llmService = LLMService()
        val memoryStorage = MemoryStorage()
        val emailService = EmailService()
        
        // Utilisation immédiate de Gemini Flash avec la nouvelle clé
        llmService.registerProvider("GEMINI", com.example.the100tral.core.ai.providers.GeminiProvider("AIzaSyBJVF0UXd2ui4XH9n3JaYZznRhc41LbuxU"), isDefault = true)
        
        // On garde le local en secours
        llmService.registerProvider("LOCAL_1", LocalLLMProvider("Llama-3-Local", "http://10.0.2.2:11434"))
        
        // Outils
        val fileTool = FileCreationTool()
        val buildTool = BuildTool()
        
        val notionKey = secretStore.getSecret("NOTION_API_KEY") ?: "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"
        val notionTool = NotionConnectorTool(notionKey)
        
        val tiktokTool = TikTokTrendTool()
        
        // Tavily Setup
        val tavilyKey = secretStore.getSecret("TAVILY_API_KEY") ?: "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"
        val tavilyService = TavilyService(tavilyKey)
        val tavilyTool = TavilySearchTool(tavilyService)
        val socialListeningTool = SocialListeningTool(tavilyService)
        
        // Nouveaux outils spécialisés (AJOUTS)
        val arxivTool = ArXivTool(tavilyService)
        val congoTool = LocalIntelligenceTool(tavilyService)
        val financialTool = FinancialAnalyzerTool()
        val sheetsTool = GoogleSheetsTool()

        // 1. Initialisation de la Hiérarchie (Chef de Projet Unique)
        val superOrchestrator = SuperOrchestrator(llmService, memoryStorage)
        val projectManager = ProjectManager(superOrchestrator, llmService, memoryStorage)
        val executiveAssistant = ExecutiveAssistant(projectManager, llmService, memoryStorage)

        // Pôle Produit (Utilise Android Studio en interne pour l'exécution)
        val productDept = ProductDevelopmentDepartment(projectManager, llmService, memoryStorage)
        val frontendAgent = FrontendAgent(productDept, llmService, memoryStorage)
        val backendAgent = BackendAgent(productDept, llmService, memoryStorage)
        backendAgent.registerTool(fileTool)
        val devOpsAgent = DevOpsAgent(productDept, llmService, memoryStorage)
        devOpsAgent.registerTool(buildTool)
        
        // Pôle Marketing & Créa (Utilise Visual Studio en interne via des requêtes ciblées)
        val marketingDept = MarketingDepartment(projectManager, llmService, memoryStorage)
        val socialMediaAgent = SocialMediaAgent(marketingDept, llmService, memoryStorage)
        val reputationAgent = DigitalReputationAgent(marketingDept, llmService, memoryStorage)
        socialMediaAgent.registerTool(tiktokTool)
        
        // Pôle Commercial (Regroupe les Sales et la Relation Client)
        val commercialDept = CommercialDepartment(projectManager, llmService, memoryStorage)
        val leadHunter = LeadHunter(commercialDept, llmService, memoryStorage)
        val customerSuccess = CustomerSuccessAgent(commercialDept, llmService, memoryStorage)
        customerSuccess.registerTool(SendEmailTool(emailService))

        // Autres Pôles de connaissance
        val financeDept = FinancialDepartment(projectManager, llmService, memoryStorage)
        val cultureDept = CultureIntelligenceDepartment(projectManager, llmService, memoryStorage)
        val academicAgent = AcademicAgent(projectManager, llmService, memoryStorage)
        val visualDept = VisualStudioDepartment(projectManager, llmService, memoryStorage)

        // 2. Configuration (Structure organisationnelle pure)
        superOrchestrator.setProjectManager(projectManager)
        superOrchestrator.setExecutiveAssistant(executiveAssistant)
        
        projectManager.registerDepartment("PRODUCT_DEV", productDept)
        projectManager.registerDepartment("MARKETING", marketingDept)
        projectManager.registerDepartment("COMMERCIAL", commercialDept)
        projectManager.registerDepartment("FINANCE", financeDept)
        projectManager.registerDepartment("WATCH_CULTURE", cultureDept)
        projectManager.registerDepartment("ACADEMIC", academicAgent)
        projectManager.registerDepartment("VISUAL_STUDIO", visualDept)
        
        // Enregistrement des outils spécialisés aux agents concernés (AJOUTS)
        academicAgent.registerTool(arxivTool)
        cultureDept.registerTool(congoTool)
        financeDept.registerTool(financialTool)
        financeDept.registerTool(sheetsTool)
        
        // Enregistrement des outils de rapportage global
        projectManager.registerTool(notionTool)
        superOrchestrator.registerTool(notionTool)
        superOrchestrator.registerTool(sheetsTool)
        
        // Enregistrement des sous-agents d'exécution
        productDept.registerSubAgent("FRONTEND", frontendAgent)
        productDept.registerSubAgent("BACKEND", backendAgent)
        productDept.registerSubAgent("DEVOPS", devOpsAgent)
        
        marketingDept.registerSubAgent("SOCIAL_MEDIA", socialMediaAgent)
        marketingDept.registerSubAgent("DIGITAL_PR", reputationAgent)
        
        socialMediaAgent.registerTool(tavilyTool)
        academicAgent.registerTool(tavilyTool)
        reputationAgent.registerTool(tavilyTool)
        reputationAgent.registerTool(socialListeningTool)
        
        commercialDept.registerSubAgent("LEADS", leadHunter)
        commercialDept.registerSubAgent("CUSTOMER_CARE", customerSuccess)

        val server = MainServer(superOrchestrator)

        setContent {
            THE100TRALTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val scope = rememberCoroutineScope()
                    MainContainer(
                        onSendCommand = { command, _ ->
                            scope.launch {
                                server.handleUserRequest(request = command, domain = "GLOBAL")
                            }
                        }
                    ) { /* ... */ }
                }
            }
        }
    }
}
