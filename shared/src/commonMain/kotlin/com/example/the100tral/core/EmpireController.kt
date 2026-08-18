package com.example.the100tral.core

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.ai.providers.*
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.platform.orchestration.SuperOrchestrator
import com.example.the100tral.platform.management.*
import com.example.the100tral.platform.departments.marketing.MarketingDepartment
import com.example.the100tral.platform.departments.sales.SalesDepartment
import com.example.the100tral.platform.departments.finance.FinancialDepartment
import com.example.the100tral.platform.departments.product.ProductDevelopmentDepartment
import com.example.the100tral.platform.departments.visual.VisualStudioDepartment
import com.example.the100tral.platform.departments.academic.AcademicAgent
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.contract.BaseAgent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object EmpireController {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized = _isInitialized.asStateFlow()

    val llmService = LLMService()
    val memory = MemoryStorage()
    
    lateinit var orchestrator: SuperOrchestrator
    lateinit var pm: ProjectManager
    var allAgents: List<BaseAgent> = emptyList()
    
    // Callback pour le lancement Windows (InjectÃ© depuis Main.kt)
    var onWakeUpIA: ((String) -> Unit)? = null

    fun init() {
        if (_isInitialized.value) return

        llmService.apply {
            registerProvider("LLAMA_3_2", LocalServerProvider("Llama 3.2", "http://localhost:1234/v1"), isDefault = true)
            registerProvider("QWEN_VISION", LocalServerProvider("Qwen 2.5 VL", "http://localhost:1235/v1"))
            registerProvider("DEEPSEEK_FLASH", LocalServerProvider("DeepSeek v3", "http://localhost:1236/v1"))
        }

        orchestrator = SuperOrchestrator(llmService, memory)
        pm = ProjectManager(orchestrator, llmService, memory)
        val quality = QualityControlAgent(pm, llmService, memory)
        val secretary = ExecutiveAssistant(orchestrator, llmService, memory)
        
        val marketing = MarketingDepartment(pm, llmService, memory)
        val sales = SalesDepartment(pm, llmService, memory)
        val product = ProductDevelopmentDepartment(pm, llmService, memory)
        val visual = VisualStudioDepartment(pm, llmService, memory)
        val academic = AcademicAgent(pm, llmService, memory)

        allAgents = listOf(orchestrator, pm, quality, secretary, marketing, sales, product, visual, academic)

        pm.setQualityAgent(quality)
        pm.setExecutiveAssistant(secretary)
        orchestrator.setProjectManager(pm)
        
        pm.registerDepartment("MARKETING", marketing)
        pm.registerDepartment("SALES", sales)
        pm.registerDepartment("PRODUCT_DEV", product)
        pm.registerDepartment("VISUAL_DESIGN", visual)
        pm.registerDepartment("ACADEMIC_RESEARCH", academic)
        
        marketing.registerSubAgent("SOCIAL_LISTENING", com.example.the100tral.platform.departments.marketing.sub.SocialMediaAgent(marketing, llmService, memory))
        
        _isInitialized.value = true
    }

    fun wakeUpIA(modelKey: String) {
        ThoughtMonitor.updateThought("SystÃ¨me", "INFRA", "Ordre de lancement reÃ§u pour : " + modelKey)
        onWakeUpIA?.invoke(modelKey)
    }

    fun handleUserRequest(message: String) {
        scope.launch {
            try {
                orchestrator.initiateMission(message)
            } catch (e: Exception) {
                ThoughtMonitor.updateThought("SystÃ¨me", "ERREUR", "Le serveur IA ne rÃ©pond pas. Lancez-le via l'onglet RÃ‰GLAGES.")
            }
        }
    }

    fun setModelForAgent(agentId: String, modelKey: String) {
        allAgents.find { it.agentIdentifier == agentId }?.preferredModel = modelKey
    }
}
