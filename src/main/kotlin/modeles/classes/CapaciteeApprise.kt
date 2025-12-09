package modeles.classes

import kotlinx.serialization.Serializable

@Serializable
data class CapaciteeApprise(
    val id : Int,
    val nom : String,
    var ppActuels: Int,
    val ppMax: Int
)
