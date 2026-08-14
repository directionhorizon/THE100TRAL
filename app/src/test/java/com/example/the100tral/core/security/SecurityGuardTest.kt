package com.example.the100tral.core.security

import org.junit.Assert.assertEquals
import org.junit.Test

class SecurityGuardTest {

    @Test
    fun `test filterInput redacts PII`() {
        val input = "Mon email est test@example.com et mon tel est 0612345678"
        val expected = "Mon email est [DATA_REDACTED] et mon tel est [DATA_REDACTED]"
        
        val result = SecurityGuard.filterInput(input)
        
        assertEquals(expected, result)
    }

    @Test
    fun `test filterInput redacts credit card`() {
        val input = "Voici ma carte : 1234 5678 1234 5678"
        val expected = "Voici ma carte : [DATA_REDACTED]"
        
        val result = SecurityGuard.filterInput(input)
        
        assertEquals(expected, result)
    }

    @Test
    fun `test filterInput redacts forbidden words`() {
        val input = "Le password est admin123"
        val expected = "Le [CONFIDENTIAL] est admin123"
        
        val result = SecurityGuard.filterInput(input)
        
        assertEquals(expected, result)
    }

    @Test
    fun `test filterOutput uses same logic as input`() {
        val output = "Contactez moi sur secret_key@gmail.com"
        val expected = "Contactez moi sur [DATA_REDACTED]"
        
        val result = SecurityGuard.filterOutput(output)
        
        assertEquals(expected, result)
    }
}
