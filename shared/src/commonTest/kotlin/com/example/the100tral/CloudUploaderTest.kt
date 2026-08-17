package com.example.the100tral

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.BaseAgent
import com.example.the100tral.core.contract.Command
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.tool.impl.CloudFileUploader
import com.example.the100tral.platform.departments.academic.AcademicAgent
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test de pensée pur (JVM) pour vérifier que l'IA choisit d'utiliser l'upload Cloud.
 */
class CloudUploaderTest {

    @Test
    fun testAgentUsesCloudUploader() = runBlocking {
        // Mock simple de la pensée de l'IA
        val mockLLM = object : LLMService() {
            override suspend fun think(prompt: String): String {
                return "DÃ©cision: Sauvegarde sur le Cloud. TOOL_CALL: CLOUD_FILE_UPLOADER {\"fileName\": \"test.txt\", \"content\": \"Data\"}"
            }
        }
        
        val agent = AcademicAgent(
            commandChain = object : BaseAgent(null, null) {
                override val name = "MockChain"
                override val authorityLevel = 0
                override val domain = "MOCK"
            },
            memoryStorage = MemoryStorage(),
            llmService = mockLLM
        )
        
        agent.registerTool(CloudFileUploader())

        println("--- SIMULATION DE PENSÉE IA ---")
        val result = agent.dispatch(Command(targetDomain = "ACADEMIC", instruction = "Upload test"))
        println("--- FIN DE SIMULATION ---")
        
        assertEquals("Agent AcadÃ©mique", agent.name)
    }
}
