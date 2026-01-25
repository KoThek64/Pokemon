package database

import modeles.enums.Type
import modeles.enums.CategorieCapacitee
import org.jetbrains.exposed.sql.Table

// Table pour les Pokémons
object PokemonTable : Table("pokemon") {
    val id = integer("id")
    val nom = varchar("nom", 50)
    val description = text("description")

    val type1 = enumerationByName("type_1", 20, Type::class)
    val type2 = enumerationByName("type_2", 20, Type::class).nullable()

    val pv = integer("pv")
    val attaque = integer("attaque")
    val defense = integer("defense")
    val attaqueSpe = integer("attaque_spe")
    val defenseSpe = integer("defense_spe")
    val vitesse = integer("vitesse")

    override val primaryKey = PrimaryKey(id)
}

// Table pour les Pokémons
object CapaciteeTable : Table("capacite") {
    val id = integer("id")
    val nom = varchar("nom", 50)
    val type = enumerationByName("type", 20, Type::class)
    val categorie = enumerationByName("categorie", 20, CategorieCapacitee::class)
    val puissance = integer("puissance").nullable()
    val precision = integer("precision").nullable()
    val pp = integer("pp")

    override val primaryKey = PrimaryKey(id)
}

// Table de jointure pour lier Pokémon et Capacités
object PokemonCapacitesTable : Table("pokemon_capacites") {
    val pokemonId = reference("pokemon_id", PokemonTable.id)
    val capaciteId = reference("capacite_id", CapaciteeTable.id)

    override val primaryKey = PrimaryKey(pokemonId, capaciteId)
}
