package sauvegarde.dto

import kotlinx.serialization.Serializable

@Serializable
data class JoueurSaveData(
    val nom: String,
    val argent: Double,
    val equipe: List<PokemonSaveData>
)

@Serializable
data class PokemonSaveData(
    val especeId: Int,
    val niveau: Int,
    val pvActuels: Int,
    val competences: List<CapaciteeSaveData>
)

@Serializable
data class CapaciteeSaveData(
    val id: Int,
    val ppActuels: Int
)
