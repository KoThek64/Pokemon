# 🎮 Jeu Pokémon en CLI

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![JDK](https://img.shields.io/badge/JDK-21-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Un jeu Pokémon développé en Kotlin pour la ligne de commande, offrant une expérience de combat au tour par tour.

> ⚠️ **Projet en développement actif** - De nombreuses fonctionnalités sont encore en cours d'implémentation.

## 📋 Table des matières

- [Fonctionnalités actuelles](#-fonctionnalités-actuelles)
- [Stack technique](#-stack-technique)
- [Installation](#-installation)
- [Architecture](#-architecture)
- [Tests](#-tests)
- [Roadmap](#-roadmap)

## ✨ Fonctionnalités actuelles

### ✅ Implémenté

#### Système de Pokémon
- **Création de Pokémon** avec stats de base
- **Système de niveaux** (1-100) avec calcul automatique des stats
- **18 types** disponibles (Eau, Feu, Plante, Électrique, etc.)
- **Double-type** supporté
- **Gestion des PV** (points de vie)
- **Système de capacités** :
  - Apprentissage de nouvelles capacités
  - Maximum de 4 capacités par Pokémon
  - Gestion des PP (points de pouvoir)
  - Soins de PP et PV

#### Système de Combat
- **Combats au tour par tour** entre joueur et adversaire
- **Calcul des dégâts** basé sur :
  - Puissance de l'attaque
  - Stats d'attaque/défense
  - Efficacité de type (x0, x0.5, x1, x2, x4)
  - Catégorie (Physique/Spécial/Statut)
  - Variabilité aléatoire (85-100%)
- **Actions disponibles** :
  - Attaquer avec une capacité
  - Changer de Pokémon
  - Fuir le combat
- **IA basique** pour l'adversaire
- **Gestion du KO** et changement automatique de Pokémon
- **Conditions de victoire/défaite**

#### Gestion des données
- **Pokedex** chargé depuis JSON (151 Pokémon actuellement)
- **CapacitéeDex** avec toutes les capacités depuis JSON
- **Recherche de Pokémon** par nom ou ID
- **Sérialisation/Désérialisation** avec Kotlinx Serialization

#### Système de Joueur
- **Pattern Singleton** pour le joueur principal
- **Équipe de Pokémon** (max 6)
- **Gestion de l'argent** (structure prête)
- **Adversaires** avec leurs propres équipes

#### Calculs de type
- **Tableau complet d'efficacité** des 18 types
- **Double-résistance** et **double-faiblesse** gérées
- **Immunités** (x0 multiplicateur)

## 🛠️ Stack technique

### Langage & Framework
- **Kotlin** 2.2.20
- **JVM** 21

### Bibliothèques
- **Kotlinx Serialization** 1.6.0 - Sérialisation JSON
- **JUnit 5** - Tests unitaires
- **Kotlin Test** - Assertions et tests

### Build & Outils
- **Gradle** 8.x (Kotlin DSL)
- **Git** - Gestion de version

### Architecture
- **Programmation orientée objet**
- **Data classes** pour les modèles
- **Sealed classes** pour les actions de combat
- **Object singleton** pour les calculs d'efficacité
- **Companion objects** pour les factories
- **Exceptions personnalisées** pour la gestion d'erreurs

### Workflow Git
- **Branches Git** pour chaque fonctionnalité/bugfix
- **GitHub Issues** pour le suivi des tâches
- **Labels GitHub** pour organiser les issues
- **Pull Requests** pour les revues de code
- **GitHub Actions** pour l'intégration continue (CI)
- **.gitignore** configuré pour Kotlin/Gradle

### Conventions de commit
Mis en place récemment pour une meilleure lisibilité de l'historique Git :
```bash
feat: ajout de nouvelles fonctionnalités
upgrade: Amélioration de fonctionnalités existantes
fix: correction de bug
docs: mise à jour de documentation
test: ajout ou modification de tests
refactor: simplification de code sans changer le comportement
chore: mise à jour de tâches annexes (CI, config, etc.)
```

👀 **Curieux de voir ma gestion de projet ?** Jetez un œil aux [Issues](../../issues) et [Pull Requests](../../pulls) du repo !

> ⚠️ **Note** : Ce projet est personnel et n'accepte pas de contributions externes.

## 📦 Installation

### Prérequis
- Java 21 ou supérieur
- Git

### Étapes d'installation

```bash
# Cloner le repository
git clone https://github.com/KoThek64/Pokemon.git
cd Pokemon

# Dans un terminal, pour compiler le projet
./gradlew build

# Lancer le jeu
./gradlew run
```

## 🏗️ Architecture

```
src/main/kotlin/
├── main.kt                          # Point d'entrée
└── modeles/
    ├── ActionDeCombat.kt            # Sealed class pour les actions
    ├── classes/
    │   ├── Adversaire.kt            # Adversaire IA
    │   ├── CapaciteeApprise.kt      # Capacité avec PP actuels
    │   ├── CapaciteeData.kt         # Données d'une capacité
    │   ├── CapaciteeDex.kt          # Base de données des capacités
    │   ├── Combat.kt                # Système de combat
    │   ├── EspecePokemon.kt         # Espèce (stats de base, types, etc.)
    │   ├── Joueur.kt                # Joueur (singleton)
    │   ├── Pokedex.kt               # Base de données des espèces
    │   ├── Pokemon.kt               # Instance de Pokémon
    │   ├── Stats.kt                 # Stats d'un Pokémon
    │   └── StatsCapacitee.kt        # Stats d'une capacité
    ├── enums/
    │   ├── CategorieCapacitee.kt    # PHYSIQUE/SPECIAL/STATUT
    │   └── Type.kt                  # 18 types Pokémon
    ├── exceptions/
    │   ├── CapaciteeException.kt
    │   ├── CombatException.kt
    │   ├── EquipePokemonException.kt
    │   ├── JoueurException.kt
    │   ├── NiveauException.kt
    │   ├── PokedexException.kt
    │   ├── PPException.kt
    │   └── PVException.kt
    ├── interfaces/
    │   └── Combattant.kt            # Interface pour Joueur/Adversaire
    └── objects/
        └── CalculEfficacite.kt      # Tableau d'efficacité des types

data/
├── pokedex.json                     # ~150 Pokémon
└── capacitee.json                   # Base des capacités
```

## 🧪 Tests

Le projet contient des tests unitaires pour les fonctionnalités principales :

```bash
# Lancer tous les tests
./gradlew test

# Lancer un test spécifique
./gradlew test --tests CapaciteeTests
./gradlew test --tests NiveauTests
./gradlew test --tests SoinsDegatsTest
```

### Couverture de tests
- ✅ Système de capacités (apprentissage, oubli, doublons)
- ✅ Système de niveaux (montée de niveau, limites)
- ✅ Soins et dégâts (PV, PP)
- ⏳ Combat (en cours)
- ⏳ Équipe Pokémon (en cours)

## 🚀 Roadmap

### 🔜 Idées de prochaines fonctionnalités

- [ ] **Système d'évolution** (Salamèche → Reptincel → Dracaufeu)
- [ ] **Menu principal** interactif
- [ ] **Sauvegarde/Chargement** de partie
- [ ] **Centre Pokémon** pour soigner l'équipe
- [ ] **Magasin** pour acheter objets/Pokéballs
- [ ] **Capture de Pokémon** sauvages
- [ ] **Statuts** (Brûlure, Paralysie, Sommeil, Poison, Gel)
- [ ] **Météo** (Pluie, Soleil, Tempête de sable, Grêle)
- [ ] **Objets** (Potions, Antidotes, Pokéballs, etc.)
- [ ] **Attaques statut** (Amélioration stats, baisse stats)
- [ ] **Capacités spéciales** (talents des Pokémon)
- [ ] **Objets tenus** par les Pokémon
- [ ] **Combat multiples** (2v2, 3v3)
- [ ] **Badges** et progression
- [ ] **Ligue Pokémon**
- [ ] **Zones d'exploration** (routes, villes)
- [ ] **Système de quêtes**
- [ ] **PNJ** et dialogues
- [ ] **Élevage** et œufs
- [ ] **Shiny** Pokémon
- [ ] **Natures** et IVs/EVs
- [ ] **Méga-évolutions**
- [ ] **Tests d'intégration** complets
- [ ] **Documentation KDoc**
- [x] **CI/CD** (GitHub Actions)
- [ ] **Logs** structurés
- [ ] **Configuration externe** (fichier config)
- [ ] **Interface graphique** (JavaFX ou Compose Desktop)
- [ ] **Mode multijoueur** local

### 🐛 Bugs connus
- Pas de validation si un Pokémon essaie d'utiliser une capacité sans PP
- L'IA adversaire choisit aléatoirement sans stratégie
- Pas de gestion des attaques qui échouent

## 📄 Licence

**Mon code** est sous licence [MIT](LICENSE) - tu peux t'en inspirer librement.

**Pokémon**, les noms, sprites et concepts appartiennent à **Nintendo / Game Freak / The Pokémon Company**. Ce projet est purement éducatif et non commercial, fait par un fan pour apprendre. 🎓

---

**Développé avec ❤️ en Kotlin**

