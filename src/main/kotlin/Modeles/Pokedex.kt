package Modeles

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Pokedex(
    val especes: List<EspecePokemon>
)