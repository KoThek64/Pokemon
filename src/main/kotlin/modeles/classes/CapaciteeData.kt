package modeles.classes

import kotlinx.serialization.Serializable
import modeles.enums.CategorieCapacitee
import modeles.classes.StatsCapacitee
import modeles.enums.Type

@Serializable
data class CapaciteeData(
    val id : Int,
    val nom : String,
    val categorie : CategorieCapacitee,
    val stats : StatsCapacitee,
    val type: Type
)
