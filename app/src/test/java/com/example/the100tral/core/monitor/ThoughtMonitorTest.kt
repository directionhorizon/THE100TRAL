package com.example.the100tral.core.monitor

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThoughtMonitorTest {

    @Test
    fun `test publish adds thought to state flow`() = runBlocking {
        val agent = "AgentTest"
        val domain = "TEST"
        val message = "Je pense donc je teste."
        
        ThoughtMonitor.publish(agent, domain, message)
        
        val currentThoughts = ThoughtMonitor.thoughts.value
        assertTrue(currentThoughts.isNotEmpty())
        assertEquals(agent, currentThoughts[0].agentName)
        assertEquals(message, currentThoughts[0].message)
    }

    @Test
    fun `test thoughts are in reverse chronological order`() = runBlocking {
        ThoughtMonitor.publish("A1", "D1", "M1")
        ThoughtMonitor.publish("A2", "D2", "M2")
        
        val currentThoughts = ThoughtMonitor.thoughts.value
        assertEquals("A2", currentThoughts[0].agentName)
        assertEquals("A1", currentThoughts[1].agentName)
    }
}
