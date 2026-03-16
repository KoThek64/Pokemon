package service

import database.CapaciteeTable
import database.PokemonCapacitesTable
import database.PokemonTable
import modeles.enums.CategorieCapacitee
import modeles.enums.Type
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object LocalDataSeeder {

    fun seed() {
        transaction {
            // Capacités
            cap(1,  "Charge",        Type.NORMAL,      CategorieCapacitee.PHYSIQUE, 40,  100, 35)
            cap(2,  "Griffe",        Type.NORMAL,      CategorieCapacitee.PHYSIQUE, 40,  100, 35)
            cap(22, "Tranche",       Type.NORMAL,      CategorieCapacitee.PHYSIQUE, 70,  100, 20)
            cap(34, "Fouet Lianes",  Type.PLANTE,      CategorieCapacitee.PHYSIQUE, 45,  100, 25)
            cap(52, "Flammèche",     Type.FEU,         CategorieCapacitee.SPECIALE, 40,  100, 25)
            cap(77, "Vampigraine",   Type.PLANTE,      CategorieCapacitee.SPECIALE, 80,  100, 10)
            cap(84, "Tonnerre",      Type.ELECTRIQUE,  CategorieCapacitee.SPECIALE, 90,  100, 15)
            cap(85, "Pistolet à O",  Type.EAU,         CategorieCapacitee.SPECIALE, 40,  100, 25)

            // Pokémon (id, nom, type1, type2, pv, atk, def, atkSpe, defSpe, vit, description)
            pok(25, "Pikachu",    Type.ELECTRIQUE, null,        35, 55, 40, 50, 50, 90, "Le Pokémon Souris.")
            pok(7,  "Carapuce",   Type.EAU,        null,        44, 48, 65, 50, 64, 43, "Le Pokémon Minuscule.")
            pok(4,  "Salamèche",  Type.FEU,        null,        39, 52, 43, 60, 50, 65, "Le Pokémon Lézard.")
            pok(1,  "Bulbizarre", Type.PLANTE,     Type.POISON, 45, 49, 49, 65, 65, 45, "Le Pokémon Graine.")

            // Liaisons Pokémon <-> Capacités
            lien(25, 84); lien(25, 1)   // Pikachu    : Tonnerre, Charge
            lien(7,  85); lien(7,  1)   // Carapuce   : Pistolet à O, Charge
            lien(4,  52); lien(4,  2)   // Salamèche  : Flammèche, Griffe
            lien(1,  34); lien(1,  77)  // Bulbizarre : Fouet Lianes, Vampigraine
        }
        println("✅ Données locales de test insérées.")
    }

    private fun cap(id: Int, nom: String, type: Type, cat: CategorieCapacitee, puissance: Int?, precision: Int?, pp: Int) {
        CapaciteeTable.insert {
            it[CapaciteeTable.id]        = id
            it[CapaciteeTable.nom]       = nom
            it[CapaciteeTable.type]      = type
            it[CapaciteeTable.categorie] = cat
            it[CapaciteeTable.puissance] = puissance
            it[CapaciteeTable.precision] = precision
            it[CapaciteeTable.pp]        = pp
        }
    }

    private fun pok(id: Int, nom: String, t1: Type, t2: Type?, pv: Int, atk: Int, def: Int, atkSpe: Int, defSpe: Int, vit: Int, desc: String) {
        PokemonTable.insert {
            it[PokemonTable.id]          = id
            it[PokemonTable.nom]         = nom
            it[PokemonTable.type1]       = t1
            it[PokemonTable.type2]       = t2
            it[PokemonTable.pv]          = pv
            it[PokemonTable.attaque]     = atk
            it[PokemonTable.defense]     = def
            it[PokemonTable.attaqueSpe]  = atkSpe
            it[PokemonTable.defenseSpe]  = defSpe
            it[PokemonTable.vitesse]     = vit
            it[PokemonTable.description] = desc
        }
    }

    private fun lien(pokemonId: Int, capaciteId: Int) {
        PokemonCapacitesTable.insert {
            it[PokemonCapacitesTable.pokemonId]  = pokemonId
            it[PokemonCapacitesTable.capaciteId] = capaciteId
        }
    }
}
