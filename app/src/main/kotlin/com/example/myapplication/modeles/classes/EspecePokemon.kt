package com.example.myapplication.modeles.classes

import com.example.myapplication.modeles.enums.Type
import kotlinx.serialization.Serializable

@Serializable
data class EspecePokemon(
    val id: Int,
    val nom: String,
    val types: List<Type>,
    val baseStats: Stats,
    val description: String,
    val capacitesDeBase: List<Int>
)