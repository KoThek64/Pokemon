package com.example.myapplication.modeles.classes

import kotlinx.serialization.Serializable
import com.example.myapplication.modeles.enums.CategorieCapacitee
import com.example.myapplication.modeles.enums.Type

@Serializable
data class CapaciteeData(
    val id : Int,
    val nom : String,
    val categorie : CategorieCapacitee,
    val stats : StatsCapacitee,
    val type: Type
)
