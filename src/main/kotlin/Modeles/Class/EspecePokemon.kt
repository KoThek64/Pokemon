package Modeles.Class

import Modeles.Enum.Capacitee
import Modeles.Enum.Type
import kotlinx.serialization.Serializable

@Serializable
data class EspecePokemon(
    val id: Int,
    val nom: String,
    val types: List<Type>,
    val baseStats: Stats,
    val description: String,
    val capacitesDeBase: List<Capacitee>
)