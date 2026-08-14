package com.example.the100tral.core.security

expect class SecureSecretStore() {
    fun saveSecret(key: String, value: String)
    fun getSecret(key: String): String?
    fun deleteSecret(key: String)
}
