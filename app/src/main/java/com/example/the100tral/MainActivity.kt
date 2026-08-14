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
import com.example.the100tral.platform.departments.finance.sub.*
import com.example.the100tral.platform.departments.commercial.CommercialDepartment
import com.example.the100tral.platform.departments.sales.sub.*
import com.example.the100tral.platform.departments.academic.AcademicAgent
import com.example.the100tral.platform.departments.visual.VisualStudioDepartment
import com.example.the100tral.platform.orchestration.SuperOrchestrator
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.ai.providers.GeminiProvider
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
        
        // 1. Initialisation de Firebase & Cloud
        FirebaseApp.initializeApp(this)
        AndroidContext.context = this
        
        ThoughtMonitor.setPersistenceListener {
            CloudBridgeProvider.saveThought(it.agentName, it.domain, it.message)
        }
        CloudBridgeProvider.migrateAll()
        
        // 2. Initialisation des Services IA
        val secretStore = SecureSecretStore()
        secretStore.saveSecret("GEMINI_API_KEY", "AIzaSyBJVF0UXd2ui4XH9n3JaYZznRhc41LbuxU")
        secretStore.saveSecret("NOTION_API_KEY", "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")
        secretStore.saveSecret("TAVILY_API_KEY", "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")
        
        val llmService = LLMService()
        llmService.registerProvider("GEMINI", GeminiProvider(secretStore.getSecret("GEMINI_API_KEY")!!), isDefault = true)
        
        val memoryStorage = MemoryStorage()
        val emailService = EmailService()
        val tavilyService = TavilyService(secretStore.getSecret("TAVILY_API_KEY")!!)

        // 3. Initialisation des Outils
        val fileTool = FileCreationTool()
        val buildTool = BuildTool()
        val notionTool = NotionConnectorTool(secretStore.getSecret("NOTION_API_KEY")!!)
        val tiktokTool = TikTokTrendTool()
        val tavilyTool = TavilySearchTool(tavilyService)
        val emailTool = SendEmailTool(emailService)
        val financialTool = FinancialAnalyzerTool()
        val sheetsTool = GoogleSheetsTool()
        val arxivTool = ArXivTool(tavilyService)

        // 4. Initialisation de la Hiérarchie (Niveau 1 & 2)
        val superOrchestrator = SuperOrchestrator(llmService, memoryStorage)
        val projectManager = ProjectManager(superOrchestrator, llmService, memoryStorage)
        val executiveAssistant = ExecutiveAssistant(projectManager, llmService, memoryStorage)
        val crisisArbitrator = CrisisArbitrator(projectManager, llmService, memoryStorage)

        // 5. Initialisation des Départements (Niveau 3)
        val productDept = ProductDevelopmentDepartment(projectManager, llmService, memoryStorage)
        val marketingDept = MarketingDepartment(projectManager, llmService, memoryStorage)
        val commercialDept = CommercialDepartment(projectManager, llmService, memoryStorage)
        val financeDept = FinancialDepartment(projectManager, llmService, memoryStorage)
        val academicAgent = AcademicAgent(projectManager, memoryStorage, llmService)
        val visualDept = VisualStudioDepartment(projectManager, llmService, memoryStorage)

        // 6. Enregistrement des Sous-Agents (Niveau 4)
        productDept.registerSubAgent("FRONTEND", FrontendAgent(productDept, llmService, memoryStorage))
        val backend = BackendAgent(productDept, llmService, memoryStorage)
        backend.registerTool(fileTool)
        productDept.registerSubAgent("BACKEND", backend)
        val devOps = DevOpsAgent(productDept, llmService, memoryStorage)
        devOps.registerTool(buildTool)
        productDept.registerSubAgent("DEVOPS", devOps)
        
        marketingDept.registerSubAgent("SOCIAL_MEDIA", SocialMediaAgent(marketingDept, llmService, memoryStorage))
        
        commercialDept.registerSubAgent("CUSTOMER_CARE", CustomerSuccessAgent(commercialDept, llmService, memoryStorage))
        commercialDept.registerSubAgent("LEADS", LeadHunter(commercialDept, llmService, memoryStorage))
        
        financeDept.registerSubAgent("AUDIT", AuditAgent(financeDept, llmService, memoryStorage))
        financeDept.registerSubAgent("ROI", ROIAgent(financeDept, llmService, memoryStorage))

        // 7. Raccordement Global
        superOrchestrator.setProjectManager(projectManager)
        superOrchestrator.setExecutiveAssistant(executiveAssistant)
        
        projectManager.registerDepartment("PRODUCT_DEV", productDept)
        projectManager.registerDepartment("MARKETING", marketingDept)
        projectManager.registerDepartment("COMMERCIAL", commercialDept)
        projectManager.registerDepartment("FINANCE", financeDept)
        projectManager.registerDepartment("ACADEMIC", academicAgent)
        projectManager.registerDepartment("VISUAL_STUDIO", visualDept)
        
        // Distribution des outils stratégiques
        academicAgent.registerTool(arxivTool)
        academicAgent.registerTool(tavilyTool)
        projectManager.registerTool(notionTool)
        superOrchestrator.registerTool(notionTool)

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
                    ) { /* Navigation mobile */ }
                }
            }
        }
    }
}
