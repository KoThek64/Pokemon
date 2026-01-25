package service

import database.PokemonTable
import io.ktor.client.call.body
import io.ktor.client.request.get
import modeles.enums.Type
import network.PokeApiClient
import network.dto.PokemonDto
import network.dto.PokemonSpeciesDto
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object PokemonSeeder {
    private val client = PokeApiClient.client

    fun isSeeded(expectedCount: Int = 151): Boolean = transaction {
        PokemonTable.selectAll().count() >= expectedCount
    }

    suspend fun seedPokemon(range : IntRange = 1..151){
        if (isSeeded(range.count())) {
            println("✅ Pokémon déjà importés, seeding ignoré.")
            return
        }
        println("Début de l'importation")

        val existingIds = transaction {
            PokemonTable.slice(PokemonTable.id)
                .selectAll()
                .map { it[PokemonTable.id] }
                .toSet() // On stocke dans un Set pour une recherche rapide
        }

        for (id in range){
            if (id in existingIds) {
                continue
            }

            try {
                val pokemonDto: PokemonDto = client.get("https://pokeapi.co/api/v2/pokemon/$id").body()

                val speciesDto: PokemonSpeciesDto = client.get("https://pokeapi.co/api/v2/pokemon-species/$id").body()
                val nomFr = speciesDto.names.find { it.language.name == "fr" }?.name ?: pokemonDto.name

                transaction {
                    PokemonTable.insertIgnore {
                        it[PokemonTable.id] = pokemonDto.id
                        it[nom] = nomFr
                        it[description] = "Pokémon de la 1ère génération importé depuis PokéAPI."

                        it[pv] = pokemonDto.stats.find { s -> s.stat.name == "hp" }?.baseStat ?: 0
                        it[attaque] = pokemonDto.stats.find { s -> s.stat.name == "attack" }?.baseStat ?: 0
                        it[defense] = pokemonDto.stats.find { s -> s.stat.name == "defense" }?.baseStat ?: 0
                        it[attaqueSpe] = pokemonDto.stats.find { s -> s.stat.name == "special-attack" }?.baseStat ?: 0
                        it[defenseSpe] = pokemonDto.stats.find { s -> s.stat.name == "special-defense" }?.baseStat ?: 0
                        it[vitesse] = pokemonDto.stats.find { s -> s.stat.name == "speed" }?.baseStat ?: 0

                        it[type1] = mapApiTypeToEnum(pokemonDto.types[0].type.name)
                        it[type2] = if (pokemonDto.types.size > 1) mapApiTypeToEnum(pokemonDto.types[1].type.name) else null
                    }
                }
                println("[#$id] $nomFr ajouté à la base")
            } catch (e : Exception){
                println("Erreur lors de l'import du pokémon ID $id : ${e.message}")
            }
        }
        println("Importation terminée !")
    }

    // Traduit le nom du type anglais de l'API vers mon Enum Type français
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
