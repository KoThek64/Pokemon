package network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO représentant un mouvement (move) dans l'API PokeApi
@Serializable
data class MoveDto(
    val id: Int,
    val name: String,
    val accuracy: Int?,
    val power: Int?,
    val pp: Int?,
    val type: NamedResource,
    @SerialName("damage_class") val damageClass: NamedResource,
    val names: List<MoveNameFr> // Liste des traductions
)

// DTO représentant le nom d'un mouvement dans une langue spécifique (ici le français)
@Serializable
data class MoveNameFr(
    val name: String,
    val language: NamedResource
)
