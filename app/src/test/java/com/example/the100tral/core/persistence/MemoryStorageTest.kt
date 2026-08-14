package com.example.the100tral.core.persistence

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class MemoryStorageTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var storage: MemoryStorage
    private val mockContext = mockk<Context>()

    @Before
    fun setup() {
        every { mockContext.filesDir } returns tempFolder.newFolder("files")
        storage = MemoryStorage(mockContext)
    }

    @Test
    fun `test save and search knowledge`() {
        val entry = MemoryEntry("TECH", "Test content")
        storage.saveKnowledge(entry)
        
        val results = storage.searchKnowledge("content")
        
        assertEquals(1, results.size)
        assertEquals("TECH", results[0].domain)
    }

    @Test
    fun `test persistence between instances`() {
        val entry = MemoryEntry("PERSIST", "Data to save")
        storage.saveKnowledge(entry)
        
        // Nouvelle instance pointant vers le même dossier
        val secondStorage = MemoryStorage(mockContext)
        val results = secondStorage.searchKnowledge("Data")
        
        assertEquals(1, results.size)
        assertEquals("Data to save", results[0].content)
    }

    @Test
    fun `test search is case insensitive`() {
        storage.saveKnowledge(MemoryEntry("UPPER", "CASE"))
        
        val results = storage.searchKnowledge("case")
        
        assertEquals(1, results.size)
    }
}
