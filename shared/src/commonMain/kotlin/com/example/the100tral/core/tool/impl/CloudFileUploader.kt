package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

/**
 * Outil léger pour uploader des fichiers/données vers Firebase Storage via REST API.
 * Permet d'éviter le stockage local lourd.
 */
class CloudFileUploader : ITool {
    override val toolName: String = "CLOUD_FILE_UPLOADER"
    override val description: String = "Upload des données vers Firebase Storage. Paramètres: fileName, content, contentType"

    private val client = HttpClient()
    private val bucket = "com-example-the100tral-66bff.firebasestorage.app"

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val fileName = params["fileName"]?.toString() ?: "unnamed_report_${System.currentTimeMillis()}.txt"
        val content = params["content"]?.toString() ?: ""
        val contentType = params["contentType"]?.toString() ?: "text/plain"

        return try {
            val url = "https://firebasestorage.googleapis.com/v0/b/$bucket/o?name=$fileName"
            
            val response: HttpResponse = client.post(url) {
                setBody(content)
                header(HttpHeaders.ContentType, contentType)
            }

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val json = Json.parseToJsonElement(responseBody).jsonObject
                val downloadToken = json["downloadTokens"]?.jsonPrimitive?.content ?: ""
                val downloadUrl = "https://firebasestorage.googleapis.com/v0/b/$bucket/o/${fileName.replace("/", "%2F")}?alt=media&token=$downloadToken"
                
                ToolResult(
                    success = true, 
                    output = "Fichier uploadé avec succès : $fileName",
                    data = mapOf("downloadUrl" to downloadUrl)
                )
            } else {
                ToolResult(success = false, output = "Erreur Firebase Storage: ${response.status}")
            }
        } catch (e: Exception) {
            ToolResult(success = false, output = "Exception lors de l'upload: ${e.message}")
        }
    }
}



