package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import com.example.the100tral.core.persistence.platformDataDir
import java.io.File

/**
 * Outil permettant aux agents de créer des fichiers physiques.
 */
class FileCreationTool() : ITool {
    override val toolName: String = "FILE_CREATOR"
    override val description: String = "Crée un fichier avec le contenu spécifié."

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val fileName = params["filename"] as? String ?: return ToolResult(success = false, output = "Nom de fichier manquant")
        val content = params["content"] as? String ?: return ToolResult(success = false, output = "Contenu manquant")

        return try {
            val directory = File(platformDataDir, "generated")
            if (!directory.exists()) directory.mkdirs()
            
            val file = File(directory, fileName)
            file.writeText(content)
            
            ToolResult(success = true, output = "Fichier '$fileName' créé avec succès dans ${file.absolutePath}")
        } catch (e: Exception) {
            ToolResult(success = false, output = "Erreur lors de la création du fichier : ${e.message}")
        }
    }
}


