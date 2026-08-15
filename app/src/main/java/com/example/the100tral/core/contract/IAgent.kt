package com.example.the100tral.core.contract

/**
 * Interface de base pour tous les agents de la plateforme.
 */
interface IAgent {
    val name: String
    val authorityLevel: Int
    val domain: String
}


