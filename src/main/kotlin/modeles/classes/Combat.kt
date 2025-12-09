package modeles.classes

import modeles.ActionDeCombat
import modeles.enums.CategorieCapacitee
import modeles.enums.Type
import modeles.exceptions.CombatException
import modeles.objects.CalculEfficacite
import kotlin.math.floor
import kotlin.random.Random

class Combat(
    private val joueur: Joueur,
    private val adversaire: Adversaire,
    private val capDex : CapaciteeDex
) {
    fun lancerCombat(){
        if (joueur.equipe.isEmpty() || adversaire.equipe.isEmpty()){
            throw CombatException("Un des deux combattant n'a pas d'équipe pokémon")
        }

        while (joueur.aEncoreDesPokemon() && adversaire.aEncoreDesPokemon()){

            while (joueur.getPokemonActif().estKO()){
                println("\nVotre Pokémon est KO !")
                println("Choisissez un Pokémon à envoyer :")

                for (i in 1 until joueur.equipe.size){
                    val p = joueur.equipe[i]
                    val etat = if (p.estKO()) "KO" else "${p.pvActuels} PV"
                    println("$i. ${p.espece.nom} ($etat)")
                }

                val choix = readln().toIntOrNull()

                if (choix != null && choix > 0 && choix < joueur.equipe.size){
                    val pokemonChoisi = joueur.equipe[choix]
                    if (pokemonChoisi.estKO()) {
                        println("Ce Pokémon est KO, il ne peut pas combattre !")
                    } else {
                        joueur.changerPokemonActif(choix)
                        println("${joueur.nom} envoie ${joueur.getPokemonActif().espece.nom} !")
                    }
                } else {
                    println("Choix invalide. Veuillez choisir un numéro valide.")
                }
            }

            if (adversaire.getPokemonActif().estKO()){
                val choixPossibles = mutableListOf<Int>()
                for (i in 1 until adversaire.equipe.size) {
                    if (!adversaire.equipe[i].estKO()) {
                        choixPossibles.add(i)
                    }
                }

                if (choixPossibles.isNotEmpty()) {
                    val choix = choixPossibles.random()
                    adversaire.changerPokemonActif(choix)
                    println("${adversaire.nom} envoie ${adversaire.getPokemonActif().espece.nom} !")
                } else {
                    // Si l'adversaire n'a plus de Pokémon, la boucle principale while(aEncoreDesPokemon) l'arrêtera
                    break
                }
            }

            val pokJ = joueur.getPokemonActif()
            val pokAdv = adversaire.getPokemonActif()

            println("\n------------------------------------------------")
            println("Votre pokémon : ${pokJ.espece.nom} (PV : ${pokJ.pvActuels}/${pokJ.stats.pv})")
            println("Adversaire    : ${pokAdv.espece.nom} (PV : ${pokAdv.pvActuels}/${pokAdv.stats.pv})")
            println("------------------------------------------------")

            val actionJoueur = joueur.choisirAction()
            val actionAdversaire = adversaire.choisirAction()

            if (actionJoueur is ActionDeCombat.Fuite){
                println("Vous avez pris la fuite !")
                break
            } else if (actionAdversaire is ActionDeCombat.Fuite){
                println("L'adversaire a pris la fuite !")
                break
            }

            jouerTour(actionJoueur, actionAdversaire)
        }

        // Fin du combat
        if (joueur.aEncoreDesPokemon()) {
            println("\nVICTOIRE ! Vous avez gagné le combat !")
        } else {
            println("\nDÉFAITE... Vous n'avez plus de Pokémon.")
        }
    }

    private fun getEfficaciteType(attaqueType: Type, defType1: Type, defType2: Type?): Double {
        var total = CalculEfficacite.getMultiplicateur(attaqueType, defType1)

        if (defType2 != null) {
            total *= CalculEfficacite.getMultiplicateur(attaqueType, defType2)
        }

        return total
    }

    private fun calculerDegat(attaquant : Pokemon, defenseur : Pokemon, idCapacitee : Int) : Int{
        val capData = capDex.getParId(idCapacitee)

        val a : Int
        val d : Int

        val typeDefenseur1 = defenseur.espece.types[0]
        val typeDefenseur2 = if(defenseur.espece.types.size > 1) defenseur.espece.types[1] else null

        when (capData.categorie){
            CategorieCapacitee.PHYSIQUE -> {
                a = attaquant.stats.attaque
                d = defenseur.stats.defense
            }
            CategorieCapacitee.SPECIALE -> {
                a = attaquant.stats.attaqueSpe
                d = defenseur.stats.defenseSpe
            }
            CategorieCapacitee.STATUS -> return 0
        }

        // Formule : ((((2 * Niveau / 5 + 2) * Puissance * A / D) / 50) + 2)
        val puissance = capData.stats.puissance.toDouble()
        val niveau = attaquant.niveau.toDouble()

        var degatsBase = (floor((2.0 * niveau / 5.0 + 2.0)).toInt() * puissance * a.toDouble() / d.toDouble())
        degatsBase = floor(degatsBase / 50.0) + 2.0

        // STAB (Same Type Attaque Bonus) + Efficacite type
        val stab = if (attaquant.espece.types.contains(capData.type)) 1.5 else 1.0
        val efficaciteType = getEfficaciteType(capData.type, typeDefenseur1, typeDefenseur2)
        val aleatoire = Random.nextDouble(0.85,1.0)
        val critique = 1.0

        return floor(degatsBase * stab * efficaciteType * aleatoire * critique).toInt()
    }

    private fun jouerTour(
        actionJoueur: ActionDeCombat,
        actionAdversaire: ActionDeCombat,
    ) {

        if (actionJoueur is ActionDeCombat.ChangerDePokemon){
            joueur.changerPokemonActif(actionJoueur.index)
            println("${joueur.nom} rappelle son Pokémon et envoie ${joueur.getPokemonActif().espece.nom} !")
        }

        if (actionAdversaire is ActionDeCombat.ChangerDePokemon){
            adversaire.changerPokemonActif(actionAdversaire.index)
            println("${adversaire.nom} rappelle son Pokémon et envoie ${adversaire.getPokemonActif().espece.nom} !")
        }

        val pokemonActJoueur = joueur.getPokemonActif()
        val pokemonActAdversaire = adversaire.getPokemonActif()

        // CAS A : Les deux attaquent (Duel de vitesse)
        if (actionJoueur is ActionDeCombat.Attaque && actionAdversaire is ActionDeCombat.Attaque){

            if (pokemonActJoueur.stats.vitesse >= pokemonActAdversaire.stats.vitesse){

                val degats = calculerDegat(pokemonActJoueur, pokemonActAdversaire, actionJoueur.capacitee.id)
                pokemonActAdversaire.subirDegats(degats)
                actionJoueur.capacitee.ppActuels-=1
                println("${pokemonActJoueur.espece.nom} inflige $degats dégâts !")

                if (!pokemonActAdversaire.estKO()){
                    val degatsAdv = calculerDegat(pokemonActAdversaire, pokemonActJoueur, actionAdversaire.capacitee.id)
                    pokemonActJoueur.subirDegats(degatsAdv)
                    actionAdversaire.capacitee.ppActuels-=1
                    println("${pokemonActAdversaire.espece.nom} riposte et inflige $degatsAdv dégâts !")
                }
            } else {

                val degatsAdv = calculerDegat(pokemonActAdversaire, pokemonActJoueur, actionAdversaire.capacitee.id)
                pokemonActJoueur.subirDegats(degatsAdv)
                actionAdversaire.capacitee.ppActuels-=1
                println("${pokemonActAdversaire.espece.nom} attaque en premier et inflige $degatsAdv dégâts !")

                if (!pokemonActJoueur.estKO()){
                    val degats = calculerDegat(pokemonActJoueur, pokemonActAdversaire, actionJoueur.capacitee.id)
                    pokemonActAdversaire.subirDegats(degats)
                    actionJoueur.capacitee.ppActuels-=1
                    println("${pokemonActJoueur.espece.nom} riposte et inflige $degats dégâts !")
                }
            }
        }

        // CAS B : Seul le joueur attaque (L'adversaire a switché)
        else if (actionJoueur is ActionDeCombat.Attaque) {

            val degats = calculerDegat(pokemonActJoueur, pokemonActAdversaire, actionJoueur.capacitee.id)
            pokemonActAdversaire.subirDegats(degats) // C'était ici votre bug : vous tapiez pokemonActJoueur !
            println("${pokemonActJoueur.espece.nom} profite du changement et inflige $degats dégâts à ${pokemonActAdversaire.espece.nom} !")
        }

        // CAS C : Seul l'adversaire attaque (Le joueur a switché)
        else if (actionAdversaire is ActionDeCombat.Attaque) {

            val degatsAdv = calculerDegat(pokemonActAdversaire, pokemonActJoueur, actionAdversaire.capacitee.id)
            pokemonActJoueur.subirDegats(degatsAdv)
            println("${pokemonActAdversaire.espece.nom} profite du changement et inflige $degatsAdv dégâts à ${pokemonActJoueur.espece.nom} !")
        }
    }
}