package com.example.the100tral.core.persistence

import com.example.the100tral.core.security.AndroidContext

actual val platformDataDir: String get() = AndroidContext.context.filesDir.absolutePath
