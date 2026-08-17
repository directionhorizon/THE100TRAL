package com.example.the100tral.core.persistence

import java.io.File

actual val platformDataDir: String get() {
    val dir = File(System.getProperty("user.home"), ".the100tral")
    if (!dir.exists()) dir.mkdirs()
    return dir.absolutePath
}
