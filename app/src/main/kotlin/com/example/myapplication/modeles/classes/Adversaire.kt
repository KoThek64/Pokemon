package com.example.myapplication.modeles.classes

import com.example.myapplication.modeles.ActionDeCombat
import com.example.myapplication.modeles.interfaces.Combattant

class Adversaire(
    override val nom: String,
    override val equipe: MutableList<Pokemon>
) : Combattant{
    fun choisirAction() : ActionDeCombat{
        val pokemonActif = getPokemonActif()
        return ActionDeCombat.Attaque(pokemonActif.competences[0])
    }
}