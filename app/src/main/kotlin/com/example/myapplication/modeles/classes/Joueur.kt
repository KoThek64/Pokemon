package com.example.myapplication.modeles.classes

import com.example.myapplication.modeles.ActionDeCombat
import com.example.myapplication.modeles.exceptions.EquipePokemonException
import com.example.myapplication.modeles.exceptions.JoueurException
import com.example.myapplication.modeles.interfaces.Combattant

class Joueur private constructor(
    override val nom : String,
    override val equipe: MutableList<Pokemon>,
    var argent : Double = 0.0
) : Combattant {

    companion object{
        private var instance: Joueur? = null

        fun creer(nom: String): Joueur {
            if (instance != null) {
                throw JoueurException("Il y a déjà un joueur créé pour cette partie")
            }
            val joueur = Joueur(nom, mutableListOf(), 0.0)
            instance = joueur
            return joueur
        }
    }

    fun ajouterPokemon(pokemon: Pokemon) : Boolean{
        if (this.equipe.size == 6){
            throw EquipePokemonException("L'équipe est déjà au complet")
        } else if (this.equipe.contains(pokemon)){
            throw EquipePokemonException("Le joueur possède déjà ce pokémon")
        }
        this.equipe.add(pokemon)
        return true
    }

    fun supprimerPokemon(pokemon: Pokemon) : Boolean{
        if (this.equipe.isEmpty()){
            throw EquipePokemonException("L'équipe pokémon du joueur est vide")
        } else if (!this.equipe.contains(pokemon)){
            throw EquipePokemonException("Le joueur ne possède pas ce pokémon")
        }
        this.equipe.remove(pokemon)
        return true
    }

    fun gagnerArgent(argent : Double) : Double{
        this.argent+= argent
        return this.argent
    }

    fun perdreArgent(argent : Double) : Double{
        if (this.argent == 0.0){
            throw JoueurException("Le joueur n'a plus d'argent")
        } else if (this.argent <= argent){
            this.argent = 0.0
            return 0.0
        }
        this.argent-=argent
        return this.argent
    }

    fun choisirAction() : ActionDeCombat{
        val pokemonActuel = equipe[0]

        println("\n--- Tour de ${nom} ---")

        while (true){
            println("\nQuelle action voulez-vous effectuer ?")
            println("1. Attaque")
            println("2. Changer de pokémon")
            println("3. Fuite")

            when(readln()) {
                "1" -> {
                    println("\nAttaques disponibles :")
                    for (i in 0 until pokemonActuel.competences.size){
                        println("$i. ${pokemonActuel.competences[i].nom} (${pokemonActuel.competences[i].ppActuels} PP)")
                    }

                    println("Choisissez une attaque (numéro) :")
                    val choix = readln().toIntOrNull()

                    if (choix != null && choix >= 0 && choix < pokemonActuel.competences.size) {
                        return ActionDeCombat.Attaque(pokemonActuel.competences[choix])
                    } else {
                        println("Choix d'attaque invalide.")
                    }
                }
                "2" -> {
                    if (equipe.size <= 1){
                        println("Vous n'avez qu'un seul Pokémon ! Impossible de changer.")
                        continue
                    }
                    val pokemonsDisponibles = equipe.subList(1, equipe.size).filter { !it.estKO() }

                    if (pokemonsDisponibles.isEmpty()) {
                        println("Tous vos autres Pokémon sont KO ! Impossible de changer.")
                        continue
                    }

                    println("\nChoisissez un Pokémon à envoyer :")
                    for (i in 1 until equipe.size){
                        val p = equipe[i]
                        val etat = if (p.estKO()) "KO" else "${p.pvActuels} PV"
                        println("$i. ${p.espece.nom} ($etat)")
                    }
                    println("0. Annuler (Retour)")

                    val choix = readln().toIntOrNull()

                    if (choix == 0) {
                        continue
                    }

                    if (choix != null && choix > 0 && choix < equipe.size){
                        val pokemonChoisi = equipe[choix]
                        if (pokemonChoisi.estKO()) {
                            println("Ce Pokémon est KO, il ne peut pas combattre !")
                        } else {
                            return ActionDeCombat.ChangerDePokemon(choix)
                        }
                    } else {
                        println("Choix de Pokémon invalide.")
                    }
                }
                "3" -> {
                    return ActionDeCombat.Fuite
                }
                else -> println("Commande inconnue. Veuillez écrire 1, 2 ou 3.")
            }
        }
    }
}