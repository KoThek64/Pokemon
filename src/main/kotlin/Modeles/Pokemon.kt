package Modeles

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val espece: EspecePokemon,
    var niveau: Int,
    var stats: Stats,
    var pvActuels: Int,
    var competences: MutableList<Capacitee>
)