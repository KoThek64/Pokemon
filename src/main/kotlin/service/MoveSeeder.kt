package service

import database.CapaciteeTable
import database.PokemonCapacitesTable
import io.ktor.client.call.*
import io.ktor.client.request.*
import modeles.enums.CategorieCapacitee
import modeles.enums.Type
import network.PokeApiClient
import network.dto.MoveDto
import network.dto.PokemonDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object MoveSeeder {
    private val client = PokeApiClient.client

    fun isSeeded(): Boolean = transaction {
        // Si on a des liaisons Pokemon-Capacités, le seeding a déjà été fait
        PokemonCapacitesTable.selectAll().count() > 0
    }

    suspend fun seedAll(range: IntRange = 1..151) {
        if (isSeeded()) {
            println("✅ Capacités et liaisons déjà importées, seeding ignoré.")
            return
        }
        println("Début de l'importation des capacités et liaisons...")

        // 1. Bulk Check des capacités déjà en base pour ne pas spammer l'API
        val existingMoveIds = transaction {
            CapaciteeTable.slice(
                CapaciteeTable.id)
                .selectAll()
                .map { it[CapaciteeTable.id] }
                .toSet()
        }

        for (pId in range) {
            try {
                // On récupère le Pokémon pour voir ses capacités
                val pokemonDto: PokemonDto = client.get("https://pokeapi.co/api/v2/pokemon/$pId").body()
                println("Analyse des capacités de ${pokemonDto.name}...")

                for (moveEntry in pokemonDto.moves) {
                    val moveUrl = moveEntry.move.url
                    val moveId = moveUrl.split("/").last { it.isNotEmpty() }.toInt()

                    // A. Si la capacité n'existe pas en base, on l'importe
                    if (moveId !in existingMoveIds) {
                        importSingleMove(moveId)
                        // On l'ajoute à notre set local pour ne pas la ré-importer au prochain Pokémon
                        (existingMoveIds as MutableSet).add(moveId)
                    }

                    // B. On crée la liaison dans pokemon_capacites (si elle n'existe pas)
                    transaction {
                        val linkExists = PokemonCapacitesTable.select {
                            (PokemonCapacitesTable.pokemonId eq pId) and (PokemonCapacitesTable.capaciteId eq moveId)
                        }.any()

                        if (!linkExists) {
                            PokemonCapacitesTable.insert {
                                it[pokemonId] = pId
                                it[capaciteId] = moveId
                            }
                        }
                    }
                }
                println("capacités de [#$pId] liées.")
            } catch (e: Exception) {
                println("Erreur sur le Pokémon $pId : ${e.message}")
            }
        }
        println("Importation des capacités terminée !")
    }

    private suspend fun importSingleMove(id: Int) {
        val dto: MoveDto = client.get("https://pokeapi.co/api/v2/move/$id").body()
        val nomFr = dto.names.find { it.language.name == "fr" }?.name ?: dto.name

        transaction {
            CapaciteeTable.insert {
                it[CapaciteeTable.id] = dto.id
                it[nom] = nomFr
                it[type] = mapApiTypeToEnum(dto.type.name)
                it[categorie] = mapApiCategoryToEnum(dto.damageClass.name)
                it[puissance] = dto.power
                it[precision] = dto.accuracy
                it[pp] = dto.pp ?: 0
            }
        }
        println("Nouvelle capacité importée : $nomFr")
    }

    private fun mapApiCategoryToEnum(apiCat: String): CategorieCapacitee {
        return when (apiCat.lowercase()) {
            "physical" -> CategorieCapacitee.PHYSIQUE
            "special" -> CategorieCapacitee.SPECIALE
            else -> CategorieCapacitee.STATUS
        }
    }

    private fun mapApiTypeToEnum(apiTypeName: String): Type {
        return when (apiTypeName.lowercase()) {
            "water" -> Type.EAU
            "fire" -> Type.FEU
            "grass" -> Type.PLANTE
            "normal" -> Type.NORMAL
            "flying" -> Type.VOL
            "steel" -> Type.ACIER
            "electric" -> Type.ELECTRIQUE
            "poison" -> Type.POISON
            "ghost" -> Type.SPECTRE
            "dark" -> Type.TENEBRE
            "dragon" -> Type.DRAGON
            "fairy" -> Type.FEE
            "psychic" -> Type.PSY
            "fighting" -> Type.COMBAT
            "rock" -> Type.ROCHE
            "ground" -> Type.SOL
            "ice" -> Type.GLACE
            "bug" -> Type.INSECTE
            else -> Type.NORMAL
        }
    }
}
