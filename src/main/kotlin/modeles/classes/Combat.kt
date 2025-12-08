package modeles.classes

import modeles.ActionDeCombat
import modeles.exceptions.CombatException

class Combat(
    private val joueur: Joueur,
    private val adversaire: Adversaire
) {
    fun lancerCombat(){
        if (joueur.equipe.isEmpty() || adversaire.equipe.isEmpty()){
            throw CombatException("Un des deux combattant n'a pas d'équipe pokémon")
        }
        while (joueur.aEncoreDesPokemon() && adversaire.aEncoreDesPokemon()){
            val actionJoueur = joueur.choisirAction()
            val actionAdversaire = adversaire.choisirAction()
            if (actionJoueur is ActionDeCombat.Fuite || actionAdversaire is ActionDeCombat.Fuite){
                break
            }
            jouerTour(actionJoueur, actionAdversaire)
        }
    }

    private fun jouerTour(actionJoueur: ActionDeCombat, actionAdversaire: ActionDeCombat) {
        if (actionJoueur is ActionDeCombat.ChangerDePokemon){
            joueur.changerPokemonActif(actionJoueur.index)
        }
        if (actionAdversaire is ActionDeCombat.ChangerDePokemon){
            adversaire.changerPokemonActif(actionAdversaire.index)
        }

        val pokemonActJoueur = joueur.getPokemonActif()
        val pokemonActAdversaire = adversaire.getPokemonActif()

        if (actionJoueur is ActionDeCombat.Attaque || actionAdversaire is ActionDeCombat.Attaque){
            if (pokemonActJoueur.stats.vitesse >= pokemonActAdversaire.stats.vitesse){
                pokemonActAdversaire.subirDegats(50)
                pokemonActJoueur.subirDegats(25)
            } else {
                pokemonActJoueur.subirDegats(25)
                pokemonActAdversaire.subirDegats(50)
            }
        }
    }
}