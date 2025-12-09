package com.example.myapplication.modeles.classes

import kotlinx.serialization.Serializable

@Serializable
data class StatsCapacitee(
    val puissance : Int,
    val precision : Int,
    val pp : Int,
    val ppMax : Int
)