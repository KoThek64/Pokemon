package modeles

import modeles.enums.Capacitee

sealed class ActionDeCombat {
    data class Attaque(val capacitee: Capacitee) : ActionDeCombat()
    data class ChangerDePokemon(val index : Int) : ActionDeCombat()
    object Fuite : ActionDeCombat()
}