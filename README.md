<h1 align="center">🎮 Pokémon CLI</h1>
<p align="center"><em>Jeu Pokémon au tour par tour en ligne de commande, développé en Kotlin avec PostgreSQL et un import automatique des données depuis PokéAPI.</em></p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/JDK-21-ED8B00?logo=openjdk&logoColor=white" alt="JDK 21">
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Gradle-8.14-02303A?logo=gradle&logoColor=white" alt="Gradle">
  <img src="https://img.shields.io/badge/License-MIT-22c55e" alt="MIT">
</p>

---

## 📋 Contexte du projet

**Pokémon CLI** est un projet personnel réalisé pour apprendre et pratiquer le développement Kotlin, en explorant la programmation orientée objet, les bases de données relationnelles, les appels API REST et les tests unitaires.

| | |
|---|---|
| **Type** | Projet personnel |
| **Auteur** | Mattys Lachaise |
| **Année** | 2025 |

> ⚠️ **Projet en développement actif** — De nombreuses fonctionnalités sont encore en cours d'implémentation.

---

## 💡 Présentation

**Pokémon CLI** est un jeu de combat Pokémon entièrement en ligne de commande. Les 151 Pokémon de la première génération et leurs capacités sont importés automatiquement depuis [PokéAPI](https://pokeapi.co/) et stockés dans une base PostgreSQL. Le jeu propose des combats au tour par tour, un système de niveaux, une gestion des capacités et une sauvegarde de partie.

### Fonctionnalités

#### Système de Pokémon
- **151 Pokémon** (1ère génération) avec leurs capacités, importés depuis PokéAPI
- **Système de niveaux** (1–100) avec calcul automatique des 6 stats : PV, ATK, DEF, ATK SPÉ, DEF SPÉ, Vitesse
- **18 types** disponibles, double-type supporté
- **4 capacités max** par Pokémon, avec gestion des PP

#### Système de Combat
- **Combats au tour par tour** joueur vs adversaire IA
- **Priorité par vitesse** : le Pokémon le plus rapide attaque en premier
- **Calcul des dégâts** (formule officielle) : puissance, stats ATK/DEF ou ATK SPÉ/DEF SPÉ selon la catégorie, STAB, efficacité de type (x0 / x0.5 / x1 / x2 / x4), variabilité aléatoire (85–100%)
- **Actions disponibles** : attaquer, changer de Pokémon, fuir
- **Gestion du KO**, changement automatique et conditions de victoire/défaite

#### Données & Sauvegarde
- **Base de données PostgreSQL** via Exposed ORM
- **Seeding intelligent** : import automatique au 1er lancement, ignoré aux suivants
- **Sauvegarde de partie** en JSON local (`saves/joueur.json`) : nom, argent, équipe complète avec niveaux, PV et PP actuels

#### Panneau d'administration BD
- **AdminBD** : outil CLI séparé pour administrer directement la base de données
- Lister, rechercher (par ID / nom / type), modifier et supprimer des Pokémon et capacités
- Gestion des liaisons Pokémon ↔ Capacités et stats rapides

---

## 🛠️ Stack technique

| Composant | Technologie |
|-----------|-------------|
| **Langage** | Kotlin 2.2.20 |
| **Runtime** | JDK 21 |
| **Interface** | CLI (terminal) |
| **Base de données** | PostgreSQL |
| **ORM** | Exposed 0.41.1 (DSL + JDBC) |
| **Client HTTP** | Ktor Client 2.3.7 (CIO + ContentNegotiation) |
| **Sérialisation** | Kotlinx Serialization 1.6.0 |
| **Build tool** | Gradle 8.14 (Kotlin DSL) |
| **Coroutines** | Kotlinx Coroutines (appels réseau asynchrones) |
| **Tests** | JUnit 5 + Kotlin Test |
| **CI/CD** | GitHub Actions |

---

## 📁 Architecture du projet

```
Pokemon/
├── data/
│   ├── pokedex.json                         # (legacy) ~151 Pokémon pour les tests
│   └── capacitee.json                       # (legacy) Base des capacités pour les tests
├── saves/
│   └── joueur.json                          # Fichier de sauvegarde (généré au runtime)
├── src/
│   ├── main/kotlin/
│   │   ├── main.kt                          # Point d'entrée : init BD, seeding, jeu
│   │   ├── AdminBD.kt                       # Panneau d'administration CLI de la BD
│   │   ├── database/
│   │   │   ├── DatabaseFactory.kt           # Connexion PostgreSQL (lit db.properties)
│   │   │   └── Table.kt                     # Tables Exposed (Pokémon, Capacités, Liaisons)
│   │   ├── network/
│   │   │   ├── PokeApiClient.kt             # Client HTTP Ktor pour PokéAPI
│   │   │   └── dto/
│   │   │       ├── PokemonDto.kt            # DTO réponses /pokemon/{id}
│   │   │       └── MoveDto.kt              # DTO réponses /move/{id}
│   │   ├── repository/
│   │   │   ├── PokemonRepository.kt         # Requêtes BD : espèces Pokémon
│   │   │   └── MoveRepository.kt            # Requêtes BD : capacités
│   │   ├── service/
│   │   │   ├── PokemonSeeder.kt             # Import des 151 Pokémon PokéAPI → BD
│   │   │   └── MoveSeeder.kt               # Import des capacités PokéAPI → BD
│   │   ├── sauvegarde/
│   │   │   ├── SauvegardeService.kt         # Sauvegarde/Chargement de partie
│   │   │   └── dto/SauvegardeDto.kt         # DTOs de sérialisation
│   │   └── modeles/
│   │       ├── ActionDeCombat.kt            # Sealed class : Attaque / Changement / Fuite
│   │       ├── classes/                     # Joueur, Pokémon, Combat, Adversaire…
│   │       ├── enums/                       # Type (18 types), CategorieCapacitee
│   │       ├── exceptions/                  # Exceptions métier personnalisées
│   │       ├── interfaces/                  # Combattant (commun à Joueur et Adversaire)
│   │       └── objects/                     # CalculEfficacite (tableau des 18 types)
│   └── test/kotlin/
│       ├── CapaciteeTests.kt               # Tests : apprentissage et oubli de capacités
│       ├── NiveauTests.kt                  # Tests : montée de niveau et limites
│       └── SoinsDegatsTest.kt              # Tests : soins et dégâts (PV)
├── .github/workflows/tests.yml             # Pipeline CI GitHub Actions
├── build.gradle.kts                        # Configuration Gradle (Kotlin DSL)
└── settings.gradle.kts
```

| Fichier / Dossier | Description |
|-------------------|-------------|
| `main.kt` | Point d'entrée : initialise la BD, lance le seeding et démarre le jeu |
| `AdminBD.kt` | Outil d'administration CLI de la base de données |
| `database/` | Connexion PostgreSQL et définition des tables Exposed |
| `network/` | Client Ktor pour PokéAPI et DTOs associés |
| `repository/` | Pattern Repository : accès aux espèces Pokémon et aux capacités |
| `service/` | Seeders : import des 151 Pokémon et de leurs capacités depuis PokéAPI |
| `sauvegarde/` | Sauvegarde et chargement de partie en JSON |
| `modeles/` | Logique métier : Pokémon, Combat, Types, Exceptions… |

---

## 🚀 Installation & Lancement

### Prérequis

- Java 21 ou supérieur
- Un serveur **PostgreSQL** accessible
- Git

### Configuration de la base de données

Créez un fichier `db.properties` à la racine du projet (ignoré par git) :

```properties
db.host=<ADRESSE_DU_SERVEUR>
db.port=5432
db.name=pokemon_db
db.user=<UTILISATEUR>
db.password=<MOT_DE_PASSE>
```

### Lancement

```bash
# 1. Cloner le dépôt
git clone https://github.com/KoThek64/Pokemon.git
cd Pokemon

# 2. Compiler le projet
./gradlew build

# 3. Lancer le jeu
./gradlew run
```

> ℹ️ Au **premier lancement**, le jeu télécharge automatiquement les 151 Pokémon et toutes leurs capacités depuis PokéAPI et les insère en base. Les lancements suivants ignorent ce seeding.

### Lancer le panneau d'administration BD

`AdminBD` est un point d'entrée séparé à lancer depuis l'IDE (IntelliJ IDEA) en exécutant la fonction `main()` dans `AdminBD.kt`.

---

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

| Domaine | État |
|---------|------|
| Système de capacités (apprentissage, oubli, doublons) | ✅ Couvert |
| Système de niveaux (montée de niveau, limites) | ✅ Couvert |
| Soins et dégâts (PV) | ✅ Couvert |
| Combat | ⏳ En cours |
| Équipe Pokémon | ⏳ En cours |

Les tests s'exécutent automatiquement à chaque push sur `master` et `dev` via **GitHub Actions**.

---

## 🔜 Roadmap

### Prochaines fonctionnalités

- [ ] Système d'évolution (Salamèche → Reptincel → Dracaufeu)
- [ ] Menu principal interactif
- [ ] Centre Pokémon pour soigner l'équipe
- [ ] Magasin (objets, Pokéballs)
- [ ] Capture de Pokémon sauvages
- [ ] Statuts (Brûlure, Paralysie, Sommeil, Poison, Gel)
- [ ] Météo (Pluie, Soleil, Tempête de sable, Grêle)
- [ ] Objets tenus par les Pokémon
- [ ] Badges et progression
- [ ] Zones d'exploration (routes, villes)
- [ ] Natures et IVs/EVs
- [ ] Tests d'intégration complets
- [ ] Documentation KDoc
- [ ] Interface graphique (JavaFX ou Compose Desktop)

### Déjà réalisé

- [x] Sauvegarde / Chargement de partie
- [x] CI/CD (GitHub Actions)
- [x] Import PokéAPI → PostgreSQL

### 🐛 Bugs connus

- L'IA adversaire choisit toujours la première capacité disponible, sans stratégie
- Précision des attaques non appliquée

---

## 🧑‍💻 Auteur

Développé par **Mattys Lachaise** — [mattys.contact@gmail.com](mailto:mattys.contact@gmail.com)

**Curieux de voir ma gestion de projet ?** Jetez un œil aux [Issues](../../issues) et [Pull Requests](../../pulls) du repo !

> ⚠️ Ce projet est personnel et n'accepte pas de contributions externes.

---

## 📃 Licence

**Mon code** est sous licence [MIT](LICENSE) — tu peux t'en inspirer librement.

**Pokémon**, les noms et concepts appartiennent à **Nintendo / Game Freak / The Pokémon Company**. Ce projet est purement éducatif et non commercial, réalisé par un fan pour apprendre.

---

<p align="center">
  Projet réalisé avec ❤️ pour apprendre le développement Kotlin
</p>
