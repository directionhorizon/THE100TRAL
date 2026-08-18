package com.example.the100tral

import java.awt.FileDialog
import java.awt.Frame

object FilePicker {
    fun pickFile(): String? {
        val dialog = FileDialog(null as Frame?, "Choisir un document pour THE 100TRAL", FileDialog.LOAD)
        dialog.isVisible = true
        return if (dialog.file != null) {
            dialog.directory + dialog.file
        } else null
    }
}
