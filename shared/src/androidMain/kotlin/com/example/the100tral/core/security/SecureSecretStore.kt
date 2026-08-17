package com.example.the100tral.core.security

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

actual class SecureSecretStore actual constructor() {

    private val context = AndroidContext.context

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_secrets",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    actual fun saveSecret(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    actual fun getSecret(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    actual fun deleteSecret(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}
