package modeles.classes

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
}