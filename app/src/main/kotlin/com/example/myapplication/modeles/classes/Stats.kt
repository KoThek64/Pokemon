package com.example.myapplication.modeles.classes

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val pv: Int,
    val attaque: Int,
    val defense: Int,
    val attaqueSpe: Int,
    val defenseSpe: Int,
    val vitesse: Int
)

