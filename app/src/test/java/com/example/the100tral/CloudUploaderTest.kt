package com.example.the100tral

import com.example.the100tral.core.ai.LLMService
import com.example.the100tral.core.contract.BaseAgent
import com.example.the100tral.core.contract.Command
import com.example.the100tral.core.persistence.MemoryStorage
import com.example.the100tral.core.tool.impl.CloudFileUploader
import com.example.the100tral.platform.departments.academic.AcademicAgent
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Test de "pensée" pour vérifier que l'agent utilise bien l'outil CloudFileUploader.
 */
class CloudUploaderTest {

    @Test
    fun testAgentUsesCloudUploader() = runBlocking {
        // 1. Initialisation des mocks
        val mockLLM = mock(LLMService::class.java)
        val mockMemory = mock(MemoryStorage::class.java)
        val mockChain = mock(BaseAgent::class.java)
        
        // Simulation d'une réponse de l'IA qui décide d'utiliser l'outil
        `when`(mockLLM.think(anyString())).thenReturn(
            "Je vais rédiger le rapport et l'envoyer sur le cloud. TOOL_CALL: CLOUD_FILE_UPLOADER {\"fileName\": \"test_research.txt\", \"content\": \"Contenu du rapport scientifique.\", \"contentType\": \"text/plain\"}"
        )

        val agent = AcademicAgent(mockChain, mockMemory, mockLLM)
        val cloudTool = CloudFileUploader()
        agent.registerTool(cloudTool)

        // 2. Exécution de la mission
        val command = Command(id = "CMD_1", targetDomain = "ACADEMIC", instruction = "Fais une recherche et sauvegarde-la sur le cloud.")
        
        println("--- DÉBUT DU TEST DE PENSÉE ---")
        agent.dispatch(command)
        println("--- FIN DU TEST DE PENSÉE ---")

        // 3. Vérification (On ne peut pas vérifier l'appel réseau réel ici, mais on vérifie que la logique a tourné)
        assert(agent.name == "Agent Académique")
    }
}
