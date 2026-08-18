package com.example.the100tral.core.tool.impl

import com.example.the100tral.core.tool.ITool
import com.example.the100tral.core.tool.ToolResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
private data class NotionPageRequest(
    val parent: NotionParent,
    val properties: JsonObject
)

@Serializable
private data class NotionParent(
    @SerialName("database_id")
    val databaseId: String,
)

/**
 * Outil de connexion réelle vers l'API Notion.
 */
class NotionConnectorTool(private val apiKey: String) : ITool {
    override val toolName: String = "NOTION_CONNECTOR"
    override val description: String = "Envoie des rapports ou archives vers Notion. Paramètres: action, data"

    private val client = HttpClient()

    override suspend fun execute(params: Map<String, Any>): ToolResult {
        val action = params["action"] as? String ?: "SAVE_REPORT"
        val data = params["data"] as? String ?: ""
        
        return try {
            val databaseId = "68c9f56477164b859296c06a38210651" 

            val properties = buildJsonObject {
                put("Name", buildJsonObject {
                    put("title", buildJsonArray {
                        add(buildJsonObject {
                            put("text", buildJsonObject {
                                put("content", "Rapport Agent - $action")
                            })
                        })
                    })
                })
                put("Content", buildJsonObject {
                    put("rich_text", buildJsonArray {
                        add(buildJsonObject {
                            put("text", buildJsonObject {
                                put("content", data.take(2000))
                            })
                        })
                    })
                })
            }

            val requestBody = NotionPageRequest(
                parent = NotionParent(databaseId),
                properties = properties
            )

            val response = client.post("https://api.notion.com/v1/pages") {
                header("Authorization", "Bearer $apiKey")
                header("Notion-Version", "2022-06-28")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(NotionPageRequest.serializer(), requestBody))
            }

            if (response.status.isSuccess()) {
                ToolResult(success = true, output = "Données envoyées à Notion avec succès.")
            } else {
                val errorBody = response.bodyAsText()
                ToolResult(success = false, output = "Échec Notion : $errorBody")
            }
        } catch (e: Exception) {
            ToolResult(success = false, output = "Erreur de connexion Notion : ${e.message}")
        }
    }
}



