package com.example.the100tral.core.config

/**
 * CONFIGURATION CENTRALE DES CLÉS API.
 * Remplacez les valeurs ci-dessous par vos propres clés.
 */
object Config {
    var GEMINI_API_KEY = ""
    var TAVILY_API_KEY = ""
    
    fun isConfigured(): Boolean = GEMINI_API_KEY.isNotEmpty() && TAVILY_API_KEY.isNotEmpty()
}

