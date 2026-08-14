package com.example.the100tral.core.ai

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LLMServiceTest {

    private lateinit var llmService: LLMService
    private val mockProvider = mockk<ILLMProvider>()

    @Before
    fun setup() {
        llmService = LLMService()
        coEvery { mockProvider.providerName } returns "MockGPT"
    }

    @Test
    fun `test register and use default provider`() = runBlocking {
        llmService.registerProvider("MOCK", mockProvider, isDefault = true)
        coEvery { mockProvider.generateContent(any()) } returns "Réponse mockée"
        
        val response = llmService.think("Bonjour")
        
        assertEquals("Réponse mockée", response)
    }

    @Test
    fun `test specific provider selection`() = runBlocking {
        val otherProvider = mockk<ILLMProvider>()
        coEvery { otherProvider.providerName } returns "OtherGPT"
        coEvery { otherProvider.generateContent(any()) } returns "Autre réponse"
        
        llmService.registerProvider("MOCK", mockProvider, isDefault = true)
        llmService.registerProvider("OTHER", otherProvider)
        
        val response = llmService.think("Test", providerKey = "OTHER")
        
        assertEquals("Autre réponse", response)
    }

    @Test
    fun `test LLMService handles missing provider`() = runBlocking {
        val response = llmService.think("Perdu")
        assertTrue(response.contains("ERREUR"))
    }
}
