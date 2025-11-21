package Modeles.Class

import kotlinx.serialization.Serializable

@Serializable
data class Pokedex(
    val especes: List<EspecePokemon>
)