package com.example.myapplication.modeles

import com.example.myapplication.modeles.classes.CapaciteeApprise

sealed class ActionDeCombat {
    data class Attaque(val capacitee: CapaciteeApprise) : ActionDeCombat()
    data class ChangerDePokemon(val index : Int) : ActionDeCombat()
    object Fuite : ActionDeCombat()
}