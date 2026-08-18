package com.example.the100tral.core.persistence

class MemoryStorage {
    private val store = mutableMapOf<String, String>()
    fun save(key: String, value: String) { store[key] = value }
    fun load(key: String): String? = store[key]
}

