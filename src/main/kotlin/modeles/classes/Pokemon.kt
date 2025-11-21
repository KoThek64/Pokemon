package modeles.classes

import modeles.enums.Capacitee
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val espece: EspecePokemon,
    var niveau: Int,
    var stats: Stats,
    var pvActuels: Int,
    var competences: MutableList<Capacitee>
){
    fun calculerStatsPokemon(espece : EspecePokemon, niveau: Int) : Stats{

    }
}