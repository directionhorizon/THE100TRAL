package com.example.the100tral.core.security

import java.io.File
import java.util.Properties

actual class SecureSecretStore actual constructor() {
    private val secretFile = File("secrets.properties")
    private val properties = Properties()

    init {
        if (secretFile.exists()) {
            secretFile.inputStream().use { properties.load(it) }
        }
    }

    actual fun saveSecret(key: String, value: String) {
        properties.setProperty(key, value)
        secretFile.outputStream().use { properties.store(it, null) }
    }

    actual fun getSecret(key: String): String? {
        return properties.getProperty(key)
    }

    actual fun deleteSecret(key: String) {
        properties.remove(key)
        secretFile.outputStream().use { properties.store(it, null) }
    }
}
