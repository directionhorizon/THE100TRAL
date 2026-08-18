# AmÃ©lioration UI/UX Windows - THE 100TRAL

Ce plan vise Ã  rendre l'application Desktop plus cohÃ©rente, ergonomique et informative, en mettant l'accent sur la collaboration entre agents.

## User Review Required

> [!IMPORTANT]
> L'onglet "Console" deviendra la page d'accueil principale pour les interactions type Chatbot.
> Un systÃ¨me de dialogue dÃ©taillÃ© sera ajoutÃ© pour inspecter les pensÃ©es des agents.

## Proposed Changes

### [UI/UX Coherence]

#### [MODIFY] [MainContainer.kt](file:///D:/android%20studio/AndroidStudioProjects/THE100TRAL/shared/src/commonMain/kotlin/com/example/the100tral/ui/MainContainer.kt)
- Inversion de l'ordre des onglets : Console (Accueil) en premier.
- Harmonisation du style des onglets.

#### [MODIFY] [CommandCenterScreen.kt](file:///D:/android%20studio/AndroidStudioProjects/THE100TRAL/shared/src/commonMain/kotlin/com/example/the100tral/ui/CommandCenterScreen.kt)
- Ajout d'une barre de dÃ©filement (VerticalScrollbar).
- Interface type Chatbot pour discuter avec le Super-Orchestrateur.
- IntÃ©gration visuelle du flux GPS simulÃ©.

#### [MODIFY] [ChatbotScreen.kt](file:///D:/android%20studio/AndroidStudioProjects/THE100TRAL/shared/src/commonMain/kotlin/com/example/the100tral/ui/ChatbotScreen.kt)
- Ajout d'une barre de dÃ©filement.
- Support du clic pour voir les dÃ©tails d'un log.

#### [NEW] [AgentDetailDialog.kt](file:///D:/android%20studio/AndroidStudioProjects/THE100TRAL/shared/src/commonMain/kotlin/com/example/the100tral/ui/components/AgentDetailDialog.kt)
- CrÃ©ation d'un composant Popup/Dialog pour afficher les dÃ©tails d'une activitÃ©.

### [Visualisation de la Collaboration]

#### [MODIFY] [OrchestratorVisualizer.kt](file:///D:/android%20studio/AndroidStudioProjects/THE100TRAL/shared/src/commonMain/kotlin/com/example/the100tral/ui/OrchestratorVisualizer.kt)
- AmÃ©lioration de la simulation GPS pour montrer le trajet de la requÃªte entre les agents.

### [QualitÃ© du Texte]

#### [MODIFY] [Tous les agents]
- VÃ©rification et correction des accents et caractÃ¨res spÃ©ciaux dans les noms et messages.

## Verification Plan

### Manual Verification
- Lancer `:desktop:run` et vÃ©rifier la prÃ©sence des barres de dÃ©filement.
- Envoyer une commande et observer le flux visuel dans la console.
- Cliquer sur une ligne d'activitÃ© pour ouvrir le panneau de dÃ©tails.
