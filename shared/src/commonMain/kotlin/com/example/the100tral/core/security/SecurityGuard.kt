package com.example.the100tral.core.security

/**
 * Gère le filtrage des entrées et sorties du LLM pour la sécurité et la confidentialité.
 */
object SecurityGuard {

    private val PII_PATTERNS = listOf(
        Regex("\\b\\d{4}[- ]?\\d{4}[- ]?\\d{4}[- ]?\\d{4}\\b"), // Carte bleue
        Regex("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"), // Email
        Regex("\\b0[1-9]([-. ]?\\d{2}){4}\\b"), // Téléphone FR
    )

    private val FORBIDDEN_WORDS = listOf("password", "secret_key", "token_admin")

    /**
     * Filtre le prompt avant envoi au LLM.
     */
    fun filterInput(input: String): String {
        var filtered = input
        PII_PATTERNS.forEach { pattern ->
            filtered = filtered.replace(pattern, "[DATA_REDACTED]")
        }
        FORBIDDEN_WORDS.forEach { word ->
            filtered = filtered.replace(word, "[CONFIDENTIAL]", ignoreCase = true)
        }
        return filtered
    }

    /**
     * Valide la réponse du LLM.
     */
    fun filterOutput(output: String): String {
        // Pour l'instant, on applique les mêmes filtres sur la sortie
        return filterInput(output)
    }
}
