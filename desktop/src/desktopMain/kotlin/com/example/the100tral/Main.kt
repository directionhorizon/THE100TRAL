package com.example.the100tral

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.*
import com.example.the100tral.ui.MainContainer
import com.example.the100tral.ui.NativeFilePicker
import com.example.the100tral.ui.theme.THE100TRALTheme
import com.example.the100tral.core.EmpireController
import com.example.the100tral.core.monitor.ThoughtMonitor
import java.io.File

fun main() {
    // BOUTON LANCER : Pilotage rÃ©el de Pinokio
    EmpireController.onWakeUpIA = { modelKey ->
        try {
            val ptermPath = "C:\\pinokio\\bin\\npm\\pterm.cmd"
            // Mapping vers les apps Pinokio rÃ©elles
            val appId = when(modelKey) {
                "DEEPSEEK" -> "deepseek-v3-server"
                "QWEN" -> "qwen-vl-server"
                else -> "llama-3-2-server"
            }
            Runtime.getRuntime().exec("$ptermPath run $appId")
            ThoughtMonitor.updateThought("SystÃ¨me", "INFRA", "DÃ©marrage de l'intelligence : $modelKey")
        } catch (e: Exception) {
            ThoughtMonitor.updateThought("SystÃ¨me", "ERREUR", "Pinokio introuvable. Lancez-le manuellement.")
        }
    }

    EmpireController.init()

    // ATTRIBUTION DES CERVEAUX PAR DEPARTEMENT
    EmpireController.setModelForAgent("Pôle Produit (DEV)", "DEEPSEEK")
    EmpireController.setModelForAgent("Chef de Projet", "DEEPSEEK")
    EmpireController.setModelForAgent("Visual Studio", "QWEN")
    // Les autres restent sur LLAMA par dÃ©faut (configurÃ© dans EmpireController)

    application {
        Window(onCloseRequest = ::exitApplication, title = "THE 100TRAL - Command Center") {
            THE100TRALTheme {
                MainContainer(
                    onSendCommand = { cmd, _ -> EmpireController.handleUserRequest(cmd) },
                    onNavigateToHierarchy = {},
                    filePicker = object : NativeFilePicker { override fun pick() = FilePicker.pickFile() },
                    agents = EmpireController.allAgents
                )
            }
        }
    }
}
