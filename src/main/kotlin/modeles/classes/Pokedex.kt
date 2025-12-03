package modeles.classes

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import modeles.exceptions.PokedexException
import java.io.File

@Serializable
data class Pokedex(
    val especes: List<EspecePokemon>
){
    companion object{
        fun chargerDepuisFichier(path: String): Pokedex{
            val json = File(path).readText()
            return Json.decodeFromString<Pokedex>(json)
        }
    }

    fun trouverEspeceParID(id : Int) : EspecePokemon {
        val espece: EspecePokemon? = especes.firstOrNull { it.id == id }

        if (espece == null){
            throw PokedexException("Le pokémon avec l'id numéro $id n'est pas dans le pokédex")
        }

        return espece
    }
}