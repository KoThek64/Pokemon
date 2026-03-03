# 🎮 Jeu Pokémon en CLI

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![JDK](https://img.shields.io/badge/JDK-21-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Un jeu Pokémon développé en Kotlin pour la ligne de commande, offrant une expérience de combat au tour par tour avec une base de données PostgreSQL et un import automatique des données depuis PokéAPI.

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

#### Base de données & Données
- **Base de données PostgreSQL** via Exposed ORM (connexion configurée par `db.properties`)
- **Import automatique depuis PokéAPI** au démarrage : 151 Pokémon et toutes leurs capacités (avec noms en français)
- **Seeding intelligent** : skip automatique si les données sont déjà en base
- **Pokedex** et **CapacitéeDex** legacy en JSON encore présents mais remplacés par la BD

#### Système de Pokémon
- **Création de Pokémon** depuis la base de données
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
  - STAB (bonus de type)
  - Catégorie (Physique/Spécial/Statut)
  - Variabilité aléatoire (85-100%)
- **Actions disponibles** :
  - Attaquer avec une capacité
  - Changer de Pokémon
  - Fuir le combat
- **IA basique** pour l'adversaire
- **Gestion du KO** et changement automatique de Pokémon
- **Conditions de victoire/défaite**

#### Sauvegarde
- **Sauvegarde de partie** en JSON local (`saves/joueur.json`)
- **Chargement de partie** au démarrage
- **DTO dédiés** pour la sérialisation (nom, argent, équipe, PP actuels)

#### Système de Joueur
- **Pattern Singleton** pour le joueur principal
- **Équipe de Pokémon** (max 6)
- **Gestion de l'argent**
- **Adversaires** avec leurs propres équipes

#### Panneau d'administration BD
- **AdminBD** : outil CLI séparé pour administrer directement la base de données
- Gestion complète des Pokémon (lister, rechercher par ID/nom/type, modifier, supprimer)
- Gestion des capacités (lister, rechercher, modifier)
- Gestion des liaisons Pokémon-Capacités
- Stats rapides (compteurs, top Pokémon par nombre de capacités)

#### Calculs de type
- **Tableau complet d'efficacité** des 18 types
- **Double-résistance** et **double-faiblesse** gérées
- **Immunités** (x0 multiplicateur)

## 🛠️ Stack technique

### Langage & Runtime
- **Kotlin** 2.2.20
- **JVM** 21
- **Coroutines** (pour les appels réseau asynchrones)

### Bibliothèques
- **Kotlinx Serialization** 1.6.0 - Sérialisation JSON
- **Exposed** 0.41.1 - ORM Kotlin pour PostgreSQL (DSL + JDBC)
- **PostgreSQL JDBC** 42.7.7 - Driver base de données
- **Ktor Client** 2.3.7 - Client HTTP pour PokéAPI (CIO engine + ContentNegotiation)
- **SLF4J Simple** 2.0.9 - Logging SQL
- **JUnit 5** - Tests unitaires
- **Kotlin Test** - Assertions et tests

### Build & Outils
- **Gradle** 8.x (Kotlin DSL)
- **Git** - Gestion de version

### Architecture
- **Programmation orientée objet**
- **Data classes** pour les modèles et DTOs
- **Sealed classes** pour les actions de combat
- **Object singleton** pour les calculs d'efficacité et les repositories
- **Companion objects** pour les factories
- **Exceptions personnalisées** pour la gestion d'erreurs
- **Pattern Repository** pour l'accès aux données
- **Seeder pattern** pour l'import initial

### Workflow Git
- **Branches Git** pour chaque fonctionnalité/bugfix
- **GitHub Issues** pour le suivi des tâches
- **Labels GitHub** pour organiser les issues
- **Pull Requests** pour les revues de code
- **GitHub Actions** pour l'intégration continue (CI)
- **.gitignore** configuré pour Kotlin/Gradle

### Conventions de commit
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
- Un serveur **PostgreSQL** accessible

### Configuration de la base de données

Créez un fichier `db.properties` à la racine du projet (il est dans le `.gitignore`) :

```properties
db.host=<ADRESSE_DU_SERVEUR>
db.port=5432
db.name=pokemon_db
db.user=<UTILISATEUR>
db.password=<MOT_DE_PASSE>
```

### Étapes d'installation

```bash
# Cloner le repository
git clone https://github.com/KoThek64/Pokemon.git
cd Pokemon

# Compiler le projet
./gradlew build

# Lancer le jeu (importe automatiquement les données depuis PokéAPI au 1er démarrage)
./gradlew run
```

> ℹ️ Au premier lancement, le jeu télécharge les 151 Pokémon et toutes leurs capacités depuis PokéAPI et les insère en base. Les lancements suivants ignorent ce seeding automatiquement.

### Lancer le panneau d'administration BD

L'`AdminBD` est un point d'entrée séparé à lancer directement depuis l'IDE (IntelliJ) en exécutant la fonction `main()` dans `AdminBD.kt`.

## 🏗️ Architecture

```
src/main/kotlin/
├── main.kt                              # Point d'entrée : init BD, seeding, jeu
├── AdminBD.kt                           # Panneau d'administration CLI de la BD
│
├── database/
│   ├── DatabaseFactory.kt               # Connexion PostgreSQL (lit db.properties)
│   └── Table.kt                         # Tables Exposed (PokemonTable, CapaciteeTable, PokemonCapacitesTable)
│
├── network/
│   ├── PokeApiClient.kt                 # Client HTTP Ktor configuré pour PokéAPI
│   └── dto/
│       ├── PokemonDto.kt                # DTO pour les réponses /pokemon/{id}
│       └── MoveDto.kt                   # DTO pour les réponses /move/{id}
│
├── repository/
│   ├── PokemonRepository.kt             # Requêtes BD : espèces Pokémon
│   └── MoveRepository.kt               # Requêtes BD : capacités
│
├── service/
│   ├── PokemonSeeder.kt                 # Import des 151 Pokémon depuis PokéAPI → BD
│   └── MoveSeeder.kt                    # Import des capacités et liaisons depuis PokéAPI → BD
│
├── sauvegarde/
│   ├── SauvegardeService.kt             # Sauvegarde/Chargement de partie (saves/joueur.json)
│   └── dto/
│       └── SauvegardeDto.kt             # DTOs de sauvegarde (JoueurSaveData, PokemonSaveData, CapaciteeSaveData)
│
└── modeles/
    ├── ActionDeCombat.kt                # Sealed class : Attaque / ChangerDePokemon / Fuite
    ├── classes/
    │   ├── Adversaire.kt                # Adversaire IA
    │   ├── CapaciteeApprise.kt          # Capacité avec PP actuels
    │   ├── CapaciteeData.kt             # Données d'une capacité
    │   ├── CapaciteeDex.kt              # (legacy) Chargement depuis JSON
    │   ├── Combat.kt                    # Système de combat au tour par tour
    │   ├── EspecePokemon.kt             # Espèce (stats de base, types, capacités)
    │   ├── Joueur.kt                    # Joueur (singleton)
    │   ├── Pokedex.kt                   # (legacy) Chargement depuis JSON
    │   ├── Pokemon.kt                   # Instance d'un Pokémon
    │   ├── Stats.kt                     # Stats d'un Pokémon
    │   └── StatsCapacitee.kt            # Stats d'une capacité
    ├── enums/
    │   ├── CategorieCapacitee.kt        # PHYSIQUE / SPECIALE / STATUS
    │   └── Type.kt                      # 18 types Pokémon
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
    │   └── Combattant.kt                # Interface commune Joueur / Adversaire
    └── objects/
        └── CalculEfficacite.kt          # Tableau complet d'efficacité des 18 types

data/
├── pokedex.json                         # (legacy) ~151 Pokémon
└── capacitee.json                       # (legacy) Base des capacités

saves/
└── joueur.json                          # Fichier de sauvegarde (généré au runtime, gitignore)
```

## 🧪 Tests

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
- [x] **Sauvegarde/Chargement** de partie
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
- L'IA adversaire choisit aléatoirement sans stratégie
- Pas de gestion des attaques qui échouent (précision non appliquée)

## 📄 Licence

**Mon code** est sous licence [MIT](LICENSE) - tu peux t'en inspirer librement.

**Pokémon**, les noms, sprites et concepts appartiennent à **Nintendo / Game Freak / The Pokémon Company**. Ce projet est purement éducatif et non commercial, fait par un fan pour apprendre. 🎓

---

**Développé avec ❤️ en Kotlin**
