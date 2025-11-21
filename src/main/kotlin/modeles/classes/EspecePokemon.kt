package modeles.classes

import modeles.enums.Capacitee
import modeles.enums.Type
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