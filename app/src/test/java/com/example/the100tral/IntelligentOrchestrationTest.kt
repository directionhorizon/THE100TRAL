package com.example.the100tral

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.monitor.ThoughtMonitor
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.platform.management.ProjectManager
import com.example.the100tral.platform.orchestration.SuperOrchestrator
import com.example.the100tral.platform.departments.product.ProductDevelopmentDepartment
import com.example.the100tral.platform.departments.marketing.MarketingDepartment
import com.example.the100tral.platform.departments.product.sub.BackendAgent
import com.example.the100tral.platform.departments.marketing.sub.SocialMediaAgent
import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class IntelligentOrchestrationTest {

    private lateinit var llmService: LLMService
    private lateinit var storage: MemoryStorage
    private lateinit var superOrchestrator: SuperOrchestrator
    private lateinit var projectManager: ProjectManager
    private lateinit var productDept: ProductDevelopmentDepartment
    private lateinit var marketingDept: MarketingDepartment

    @Before
    fun setup() {
        val context = mockk<Context>()
        val tempDir = File("temp_test_orchestration")
        if (!tempDir.exists()) tempDir.mkdirs()
        every { context.filesDir } returns tempDir
        
        llmService = LLMService()
        storage = MemoryStorage(context)
        
        superOrchestrator = SuperOrchestrator(llmService, storage)
        projectManager = ProjectManager(superOrchestrator, llmService, storage)
        productDept = ProductDevelopmentDepartment(projectManager, llmService, storage)
        marketingDept = MarketingDepartment(projectManager, llmService, storage)

        superOrchestrator.setProjectManager(projectManager)
        projectManager.registerDepartment("PRODUCT_DEV", productDept)
        projectManager.registerDepartment("MARKETING", marketingDept)
        
        val backendAgent = BackendAgent(productDept, llmService, storage)
        val socialAgent = SocialMediaAgent(marketingDept, llmService, storage)
        
        productDept.registerSubAgent("BACKEND", backendAgent)
        marketingDept.registerSubAgent("SOCIAL_MEDIA", socialAgent)
    }

    @Test
    fun `demonstration du raisonnement et de la division`() = runBlocking {
        // Mission complexe nécessitant deux départements
        val mission = "Crée un script technique et prépare la pub LinkedIn"
        
        println("Lancement de la mission : $mission")
        superOrchestrator.initiateMission(mission, "GLOBAL")
        
        val thoughts = ThoughtMonitor.thoughts.value
        
        // Affichage du flux pour la démonstration
        println("\n--- FLUX DES PENSÉES DES AGENTS ---")
        thoughts.reversed().forEach { 
            println("[${it.agentName} | ${it.domain}] -> ${it.message}")
        }

        // Vérification de la DIVISION par le ProjectManager
        assertTrue("Le processus devrait montrer une subdivision", 
            thoughts.any { it.message.contains("subdivision") || it.message.contains("sous-tâche") })
        
        // Vérification du RAISONNEMENT
        assertTrue("Les agents devraient montrer des cycles de réflexion", 
            thoughts.any { it.message.contains("Réflexion") })
    }
}
