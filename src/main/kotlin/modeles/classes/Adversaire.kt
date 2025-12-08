package modeles.classes

import modeles.ActionDeCombat
import modeles.interfaces.Combattant

class Adversaire(
    override val nom: String,
    override val equipe: MutableList<Pokemon>
) : Combattant{
    fun choisirAction() : ActionDeCombat{
        val pokemonActif = getPokemonActif()
        return ActionDeCombat.Attaque(pokemonActif.competences[0])
    }
}