package repository

import database.PokemonCapacitesTable
import database.PokemonTable
import modeles.classes.EspecePokemon
import modeles.classes.Stats
import modeles.enums.Type
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object PokemonRepository {

    fun trouverEspeceParID(id: Int): EspecePokemon = transaction {
        PokemonTable.select { PokemonTable.id eq id }
            .map { rowToEspece(it) }
            .singleOrNull() ?: error("Le pokémon avec l'id numéro $id n'est pas sur le Cuby")
    }

    fun trouverEspeceParNom(nom: String): EspecePokemon = transaction {
        PokemonTable.select { PokemonTable.nom eq nom }
            .map { rowToEspece(it) }
            .singleOrNull() ?: error("Le pokémon avec le nom $nom n'est pas sur le Cuby")
    }

    fun rechercherParType(type: Type): List<EspecePokemon> = transaction {
        PokemonTable.select { (PokemonTable.type1 eq type) or (PokemonTable.type2 eq type) }
            .orderBy(PokemonTable.id)
            .map { rowToEspece(it) }
    }

    fun all(): List<EspecePokemon> = transaction {
        PokemonTable.selectAll().orderBy(PokemonTable.id).map { rowToEspece(it) }
    }

    private fun rowToEspece(row: ResultRow): EspecePokemon {
        val idPokemon = row[PokemonTable.id]

        // On récupère les IDs des capacités depuis la table de liaison
        val idsCapacites = PokemonCapacitesTable
            .select { PokemonCapacitesTable.pokemonId eq idPokemon }
            .map { it[PokemonCapacitesTable.capaciteId] }

        return EspecePokemon(
            idPokemon,
            row[PokemonTable.nom],
            listOfNotNull(row[PokemonTable.type1], row[PokemonTable.type2]),
            Stats(
                row[PokemonTable.pv],
                row[PokemonTable.attaque],
                row[PokemonTable.defense],
                row[PokemonTable.attaqueSpe],
                row[PokemonTable.defenseSpe],
                row[PokemonTable.vitesse]
            ),
            row[PokemonTable.description],
            idsCapacites.toMutableList()
        )
    }
}
