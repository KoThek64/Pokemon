package network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO pour les détails d'un Pokémon
@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val stats: List<StatSlot>,
    val types: List<TypeSlot>,
    val moves: List<MoveEntryDto>
)

// Sous-structures pour les statistiques et les types
@Serializable
data class StatSlot(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NamedResource
)

// Structure pour les types de Pokémon
@Serializable
data class TypeSlot(
    val slot: Int,
    val type: NamedResource
)

// Structure pour les ressources nommées
@Serializable
data class NamedResource(
    val name: String,
    val url: String
)

// DTO pour les espèces de Pokémon avec les noms localisés
@Serializable
data class PokemonSpeciesDto(
    val names: List<LanguageName>
)

// Structure pour les noms dans différentes langues
@Serializable
data class LanguageName(
    val name: String,
    val language: NamedResource
)

// Structure pour les capacités du pokémon
@Serializable
data class MoveEntryDto(
    val move: NamedResource
)
