package com.example.the100tral

import com.example.the100tral.core.contract.*
import com.example.the100tral.core.persistence.MemoryEntry
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.platform.management.ProjectManager
import com.example.the100tral.platform.orchestration.SuperOrchestrator
import com.example.the100tral.platform.departments.product.ProductDevelopmentDepartment
import com.example.the100tral.platform.departments.product.sub.BackendAgent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import android.content.Context
import java.io.File

class PlatformHierarchyTest {

    private lateinit var storage: MemoryStorage
    private lateinit var superOrchestrator: SuperOrchestrator
    private lateinit var projectManager: ProjectManager
    private lateinit var productDept: ProductDevelopmentDepartment
    private lateinit var backendAgent: BackendAgent

    @Before
    fun setup() {
        // Mock Context pour MemoryStorage
        val context = mock(Context::class.java)
        val tempDir = File("temp_test_memory")
        if (!tempDir.exists()) tempDir.mkdirs()
        org.mockito.Mockito.`when`(context.filesDir).thenReturn(tempDir)
        
        storage = MemoryStorage(context)
        superOrchestrator = SuperOrchestrator(null, storage)
        projectManager = ProjectManager(superOrchestrator, null, storage)
        productDept = ProductDevelopmentDepartment(projectManager, null, storage)
        backendAgent = BackendAgent(productDept, null, storage)

        superOrchestrator.setProjectManager(projectManager)
        projectManager.registerDepartment("PRODUCT_DEV", productDept)
        productDept.registerSubAgent("BACKEND", backendAgent)
    }

    @Test
    fun `test complete command chain from N1 to N4`() = runBlocking {
        val mission = "Test Architecture Integration"
        val domain = "PRODUCT_DEV"
        
        superOrchestrator.initiateMission(mission, domain)
        
        val thoughts = ThoughtMonitor.thoughts.value
        
        // Vérification de la propagation
        assertTrue(thoughts.any { it.message.contains("Initialisation de la vision stratégique") })
        assertTrue(thoughts.any { it.message.contains("Délégation de la mission au Chef de Projet") })
        assertTrue(thoughts.any { it.message.contains("Réception de l'objectif stratégique") })
        assertTrue(thoughts.any { it.message.contains("Délégation au département : PRODUCT_DEV") })
    }

    @Test
    fun `test knowledge sharing between agents`() = runBlocking {
        val entry = MemoryEntry("RESEARCH", "L'IA multi-agent est le futur.")
        storage.saveKnowledge(entry)
        
        val found = storage.searchKnowledge("RESEARCH")
        assertEquals(1, found.size)
        assertEquals(entry.content, found[0].content)
    }
}
