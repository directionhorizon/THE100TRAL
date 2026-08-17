# Plan de Déploiement Local THE100TRAL

Ce plan vise à obtenir une version exécutable fonctionnelle sur Windows et Android en respectant l'architecture multiplateforme existante.

## Objectifs
- Compiler et générer l'exécutable Windows (:desktop).
- Compiler et générer l'APK Android (:app).
- Résoudre les conflits de configuration sans changer l'architecture.

## Changements Proposés

### Configuration Globale
#### [MODIFY] [gradle.properties](file:///D:/android studio/AndroidStudioProjects/THE100TRAL/gradle.properties)
- Stabilisation des chemins de cache sur le disque D: pour éviter la saturation de C:.
- Fixation du JDK 21.

### Module Partagé
#### [MODIFY] [shared/build.gradle.kts](file:///D:/android studio/AndroidStudioProjects/THE100TRAL/shared/build.gradle.kts)
- Vérification et correction des dépendances KMP pour la cible Desktop.

## Plan de Vérification

### Compilation Windows
- Commande : `gradlew :desktop:createDistributable`
- Résultat attendu : Exécutable dans `desktop/build/compose/binaries/main/app/`

### Compilation Android
- Commande : `gradlew :app:assembleDebug`
- Résultat attendu : APK dans `app/build/outputs/apk/debug/`

### Validation Finale
1. Lancement de l'app Windows.
2. Installation de l'APK sur device/émulateur.
