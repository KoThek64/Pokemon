import database.CapaciteeTable
import database.DatabaseFactory
import database.PokemonCapacitesTable
import database.PokemonTable
import modeles.enums.Type
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    DatabaseFactory.init()
    println("=== Admin BD Pokémon ===\n")
    menuPrincipal()
}

fun menuPrincipal() {
    while (true) {
        println("\n╔════════════════════════════════╗")
        println("║       MENU PRINCIPAL           ║")
        println("╠════════════════════════════════╣")
        println("║ 1. Pokémon                     ║")
        println("║ 2. Capacités                   ║")
        println("║ 3. Liaisons Pokémon-Capacités  ║")
        println("║ 4. Stats rapides               ║")
        println("║ 0. Quitter                     ║")
        println("╚════════════════════════════════╝")
        print("Choix : ")

        when (readln()) {
            "1" -> menuPokemon()
            "2" -> menuCapacites()
            "3" -> menuLiaisons()
            "4" -> statsRapides()
            "0" -> {
                println("Au revoir !")
                return
            }
            else -> println("Choix invalide")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// MENU POKÉMON
// ═══════════════════════════════════════════════════════════════

fun menuPokemon() {
    while (true) {
        println("\n--- POKÉMON ---")
        println("1. Lister tous")
        println("2. Rechercher par ID")
        println("3. Rechercher par nom")
        println("4. Rechercher par type")
        println("5. Modifier un Pokémon")
        println("6. Supprimer un Pokémon")
        println("0. Retour")
        print("Choix : ")

        when (readln()) {
            "1" -> listerPokemon()
            "2" -> rechercherPokemonParId()
            "3" -> rechercherPokemonParNom()
            "4" -> rechercherPokemonParType()
            "5" -> modifierPokemon()
            "6" -> supprimerPokemon()
            "0" -> return
            else -> println("Choix invalide")
        }
    }
}

fun listerPokemon() {
    print("Limite (vide = tout) : ")
    val limite = readln().toIntOrNull()

    transaction {
        var query = PokemonTable.selectAll().orderBy(PokemonTable.id)
        if (limite != null) query = query.limit(limite)
        query.forEach { row ->
                val types = listOfNotNull(row[PokemonTable.type1], row[PokemonTable.type2]).joinToString("/")
                println("#${row[PokemonTable.id].toString().padStart(3, '0')} ${row[PokemonTable.nom].padEnd(15)} [$types]")
            }
    }
}

fun rechercherPokemonParId() {
    print("ID : ")
    val id = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        PokemonTable.select { PokemonTable.id eq id }
            .firstOrNull()
            ?.let { afficherPokemonDetail(it) }
            ?: println("Pokémon non trouvé")
    }
}

fun rechercherPokemonParNom() {
    print("Nom (ou partie) : ")
    val nom = readln()

    transaction {
        PokemonTable.select { PokemonTable.nom.lowerCase() like "%${nom.lowercase()}%" }
            .forEach { afficherPokemonDetail(it) }
    }
}

fun rechercherPokemonParType() {
    println("Types disponibles : ${Type.entries.joinToString(", ")}")
    print("Type : ")
    val typeStr = readln().uppercase()
    val type = try { Type.valueOf(typeStr) } catch (_: Exception) { return println("Type invalide") }

    transaction {
        PokemonTable.select { (PokemonTable.type1 eq type) or (PokemonTable.type2 eq type) }
            .orderBy(PokemonTable.id)
            .forEach { row ->
                println("#${row[PokemonTable.id].toString().padStart(3, '0')} ${row[PokemonTable.nom]}")
            }
    }
}

fun afficherPokemonDetail(row: ResultRow) {
    println("\n┌─────────────────────────────────")
    println("│ #${row[PokemonTable.id]} ${row[PokemonTable.nom]}")
    println("│ Types: ${row[PokemonTable.type1]}${row[PokemonTable.type2]?.let { " / $it" } ?: ""}")
    println("│ PV: ${row[PokemonTable.pv]} | ATK: ${row[PokemonTable.attaque]} | DEF: ${row[PokemonTable.defense]}")
    println("│ ATK SPE: ${row[PokemonTable.attaqueSpe]} | DEF SPE: ${row[PokemonTable.defenseSpe]} | VIT: ${row[PokemonTable.vitesse]}")
    println("│ ${row[PokemonTable.description]}")
    println("└─────────────────────────────────")
}

fun modifierPokemon() {
    print("ID du Pokémon à modifier : ")
    val id = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        val pokemon = PokemonTable.select { PokemonTable.id eq id }.firstOrNull()
            ?: return@transaction println("Pokémon non trouvé")

        afficherPokemonDetail(pokemon)

        println("\nQue voulez-vous modifier ?")
        println("1. Nom")
        println("2. Stats (PV, ATK, DEF, etc.)")
        println("3. Types")
        println("0. Annuler")
        print("Choix : ")

        when (readln()) {
            "1" -> {
                print("Nouveau nom : ")
                val nouveauNom = readln()
                PokemonTable.update({ PokemonTable.id eq id }) {
                    it[nom] = nouveauNom
                }
                println("Nom mis à jour !")
            }
            "2" -> {
                print("PV (${pokemon[PokemonTable.pv]}) : ")
                val pv = readln().toIntOrNull() ?: pokemon[PokemonTable.pv]
                print("ATK (${pokemon[PokemonTable.attaque]}) : ")
                val atk = readln().toIntOrNull() ?: pokemon[PokemonTable.attaque]
                print("DEF (${pokemon[PokemonTable.defense]}) : ")
                val def = readln().toIntOrNull() ?: pokemon[PokemonTable.defense]
                print("ATK SPE (${pokemon[PokemonTable.attaqueSpe]}) : ")
                val atkSpe = readln().toIntOrNull() ?: pokemon[PokemonTable.attaqueSpe]
                print("DEF SPE (${pokemon[PokemonTable.defenseSpe]}) : ")
                val defSpe = readln().toIntOrNull() ?: pokemon[PokemonTable.defenseSpe]
                print("VIT (${pokemon[PokemonTable.vitesse]}) : ")
                val vit = readln().toIntOrNull() ?: pokemon[PokemonTable.vitesse]

                PokemonTable.update({ PokemonTable.id eq id }) {
                    it[PokemonTable.pv] = pv
                    it[attaque] = atk
                    it[defense] = def
                    it[attaqueSpe] = atkSpe
                    it[defenseSpe] = defSpe
                    it[vitesse] = vit
                }
                println("Stats mises à jour !")
            }
            "3" -> {
                println("Types disponibles : ${Type.entries.joinToString(", ")}")
                print("Type 1 : ")
                val type1 = try { Type.valueOf(readln().uppercase()) } catch (_: Exception) { return@transaction println("Type invalide") }
                print("Type 2 (vide si aucun) : ")
                val type2Str = readln()
                val type2 = if (type2Str.isBlank()) null else try { Type.valueOf(type2Str.uppercase()) } catch (_: Exception) { return@transaction println("Type invalide") }

                PokemonTable.update({ PokemonTable.id eq id }) {
                    it[PokemonTable.type1] = type1
                    it[PokemonTable.type2] = type2
                }
                println("Types mis à jour !")
            }
        }
    }
}

fun supprimerPokemon() {
    print("ID du Pokémon à supprimer : ")
    val id = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        val pokemon = PokemonTable.select { PokemonTable.id eq id }.firstOrNull()
            ?: return@transaction println("Pokémon non trouvé")

        println("Supprimer ${pokemon[PokemonTable.nom]} ? (o/n)")
        if (readln().lowercase() == "o") {
            // Supprimer les liaisons d'abord
            PokemonCapacitesTable.deleteWhere { pokemonId eq id }
            PokemonTable.deleteWhere { PokemonTable.id eq id }
            println("Pokémon supprimé !")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// MENU CAPACITÉS
// ═══════════════════════════════════════════════════════════════

fun menuCapacites() {
    while (true) {
        println("\n--- CAPACITÉS ---")
        println("1. Lister toutes")
        println("2. Rechercher par ID")
        println("3. Rechercher par nom")
        println("4. Rechercher par type")
        println("5. Modifier une capacité")
        println("0. Retour")
        print("Choix : ")

        when (readln()) {
            "1" -> listerCapacites()
            "2" -> rechercherCapaciteParId()
            "3" -> rechercherCapaciteParNom()
            "4" -> rechercherCapaciteParType()
            "5" -> modifierCapacite()
            "0" -> return
            else -> println("Choix invalide")
        }
    }
}

fun listerCapacites() {
    print("Limite (vide = tout) : ")
    val limite = readln().toIntOrNull()

    transaction {
        var query = CapaciteeTable.selectAll().orderBy(CapaciteeTable.id)
        if (limite != null) query = query.limit(limite)
        query.forEach { row ->
                val puissance = row[CapaciteeTable.puissance]?.toString() ?: "-"
                println("#${row[CapaciteeTable.id].toString().padStart(3, '0')} ${row[CapaciteeTable.nom].padEnd(20)} [${row[CapaciteeTable.type]}] PWR:$puissance")
            }
    }
}

fun rechercherCapaciteParId() {
    print("ID : ")
    val id = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        CapaciteeTable.select { CapaciteeTable.id eq id }
            .firstOrNull()
            ?.let { afficherCapaciteDetail(it) }
            ?: println("Capacité non trouvée")
    }
}

fun rechercherCapaciteParNom() {
    print("Nom (ou partie) : ")
    val nom = readln()

    transaction {
        CapaciteeTable.select { CapaciteeTable.nom.lowerCase() like "%${nom.lowercase()}%" }
            .forEach { afficherCapaciteDetail(it) }
    }
}

fun rechercherCapaciteParType() {
    println("Types disponibles : ${Type.entries.joinToString(", ")}")
    print("Type : ")
    val typeStr = readln().uppercase()
    val type = try { Type.valueOf(typeStr) } catch (_: Exception) { return println("Type invalide") }

    transaction {
        CapaciteeTable.select { CapaciteeTable.type eq type }
            .orderBy(CapaciteeTable.id)
            .forEach { row ->
                val puissance = row[CapaciteeTable.puissance]?.toString() ?: "-"
                println("#${row[CapaciteeTable.id]} ${row[CapaciteeTable.nom]} (PWR: $puissance)")
            }
    }
}

fun afficherCapaciteDetail(row: ResultRow) {
    println("\n┌─────────────────────────────────")
    println("│ #${row[CapaciteeTable.id]} ${row[CapaciteeTable.nom]}")
    println("│ Type: ${row[CapaciteeTable.type]} | Catégorie: ${row[CapaciteeTable.categorie]}")
    println("│ Puissance: ${row[CapaciteeTable.puissance] ?: "-"} | Précision: ${row[CapaciteeTable.precision] ?: "-"} | PP: ${row[CapaciteeTable.pp]}")
    println("└─────────────────────────────────")
}

fun modifierCapacite() {
    print("ID de la capacité à modifier : ")
    val id = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        val cap = CapaciteeTable.select { CapaciteeTable.id eq id }.firstOrNull()
            ?: return@transaction println("Capacité non trouvée")

        afficherCapaciteDetail(cap)

        println("\nNouvelles valeurs (vide = garder l'actuelle) :")
        print("Puissance (${cap[CapaciteeTable.puissance]}) : ")
        val puissance = readln().toIntOrNull() ?: cap[CapaciteeTable.puissance]
        print("Précision (${cap[CapaciteeTable.precision]}) : ")
        val precision = readln().toIntOrNull() ?: cap[CapaciteeTable.precision]
        print("PP (${cap[CapaciteeTable.pp]}) : ")
        val pp = readln().toIntOrNull() ?: cap[CapaciteeTable.pp]

        CapaciteeTable.update({ CapaciteeTable.id eq id }) {
            it[CapaciteeTable.puissance] = puissance
            it[CapaciteeTable.precision] = precision
            it[CapaciteeTable.pp] = pp
        }
        println("Capacité mise à jour !")
    }
}

// ═══════════════════════════════════════════════════════════════
// MENU LIAISONS
// ═══════════════════════════════════════════════════════════════

fun menuLiaisons() {
    while (true) {
        println("\n--- LIAISONS POKÉMON-CAPACITÉS ---")
        println("1. Voir capacités d'un Pokémon")
        println("2. Voir Pokémon ayant une capacité")
        println("3. Ajouter une liaison")
        println("4. Supprimer une liaison")
        println("0. Retour")
        print("Choix : ")

        when (readln()) {
            "1" -> voirCapacitesPokemon()
            "2" -> voirPokemonCapacite()
            "3" -> ajouterLiaison()
            "4" -> supprimerLiaison()
            "0" -> return
            else -> println("Choix invalide")
        }
    }
}

fun voirCapacitesPokemon() {
    print("ID ou nom du Pokémon : ")
    val input = readln()
    val pokemonId = input.toIntOrNull()

    transaction {
        val pokemon = if (pokemonId != null) {
            PokemonTable.select { PokemonTable.id eq pokemonId }.firstOrNull()
        } else {
            PokemonTable.select { PokemonTable.nom.lowerCase() eq input.lowercase() }.firstOrNull()
        } ?: return@transaction println("Pokémon non trouvé")

        val id = pokemon[PokemonTable.id]
        println("\nCapacités de ${pokemon[PokemonTable.nom]} :")

        (PokemonCapacitesTable innerJoin CapaciteeTable)
            .select { PokemonCapacitesTable.pokemonId eq id }
            .orderBy(CapaciteeTable.id)
            .forEach { row ->
                println("  - #${row[CapaciteeTable.id]} ${row[CapaciteeTable.nom]} [${row[CapaciteeTable.type]}]")
            }
    }
}

fun voirPokemonCapacite() {
    print("ID de la capacité : ")
    val capId = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        val cap = CapaciteeTable.select { CapaciteeTable.id eq capId }.firstOrNull()
            ?: return@transaction println("Capacité non trouvée")

        println("\nPokémon pouvant apprendre ${cap[CapaciteeTable.nom]} :")

        (PokemonCapacitesTable innerJoin PokemonTable)
            .select { PokemonCapacitesTable.capaciteId eq capId }
            .orderBy(PokemonTable.id)
            .forEach { row ->
                println("  - #${row[PokemonTable.id]} ${row[PokemonTable.nom]}")
            }
    }
}

fun ajouterLiaison() {
    print("ID du Pokémon : ")
    val pokemonId = readln().toIntOrNull() ?: return println("ID invalide")
    print("ID de la capacité : ")
    val capId = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        val existe = PokemonCapacitesTable.select {
            (PokemonCapacitesTable.pokemonId eq pokemonId) and (PokemonCapacitesTable.capaciteId eq capId)
        }.any()

        if (existe) {
            println("Cette liaison existe déjà !")
        } else {
            PokemonCapacitesTable.insert {
                it[PokemonCapacitesTable.pokemonId] = pokemonId
                it[capaciteId] = capId
            }
            println("Liaison ajoutée !")
        }
    }
}

fun supprimerLiaison() {
    print("ID du Pokémon : ")
    val pokemonId = readln().toIntOrNull() ?: return println("ID invalide")
    print("ID de la capacité : ")
    val capId = readln().toIntOrNull() ?: return println("ID invalide")

    transaction {
        val deleted = PokemonCapacitesTable.deleteWhere {
            (PokemonCapacitesTable.pokemonId eq pokemonId) and (capaciteId eq capId)
        }
        if (deleted > 0) {
            println("Liaison supprimée !")
        } else {
            println("Liaison non trouvée")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// STATS RAPIDES
// ═══════════════════════════════════════════════════════════════

fun statsRapides() {
    transaction {
        val nbPokemon = PokemonTable.selectAll().count()
        val nbCapacites = CapaciteeTable.selectAll().count()
        val nbLiaisons = PokemonCapacitesTable.selectAll().count()

        println("\n╔════════════════════════════════╗")
        println("║       STATISTIQUES BD          ║")
        println("╠════════════════════════════════╣")
        println("║ Pokémon      : ${nbPokemon.toString().padStart(6)}         ║")
        println("║ Capacités    : ${nbCapacites.toString().padStart(6)}         ║")
        println("║ Liaisons     : ${nbLiaisons.toString().padStart(6)}         ║")
        println("╚════════════════════════════════╝")

        println("\nTop 5 Pokémon avec le plus de capacités :")
        PokemonCapacitesTable
            .slice(PokemonCapacitesTable.pokemonId, PokemonCapacitesTable.pokemonId.count())
            .selectAll()
            .groupBy(PokemonCapacitesTable.pokemonId)
            .orderBy(PokemonCapacitesTable.pokemonId.count() to SortOrder.DESC)
            .limit(5)
            .forEach { row ->
                val pId = row[PokemonCapacitesTable.pokemonId]
                val count = row[PokemonCapacitesTable.pokemonId.count()]
                val nom = PokemonTable.select { PokemonTable.id eq pId }
                    .first()[PokemonTable.nom]
                println("  $nom : $count capacités")
            }
    }
}
