import modeles.classes.*
import modeles.enums.Type

fun main() {
    try {
        lancerJeu()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun lancerJeu() {
    val pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")
    val capaciteeDex = CapaciteeDex.chargerDepuisFichier("data/capacitee.json")

    val pikachu = Pokemon.creer(pokedex.trouverEspeceParNom("Pikachu"), 50, capaciteeDex)
    println(pikachu.pvActuels)
    println(pikachu.competences)
    println(pikachu.stats)
    println(pikachu.espece)
    println(pikachu.niveau)
    println(pikachu.competences)

    pikachu.apprendreCapacitee(22, capaciteeDex)

    println(pikachu.competences)

//    val salameche = Pokemon.creer(pokedex.trouverEspeceParNom("Salamèche"), 50, capaciteeDex)
//    val carapuce = Pokemon.creer(pokedex.trouverEspeceParNom("Carapuce"), 50, capaciteeDex)
//
//    val adversaire1 = Adversaire("claude", mutableListOf(salameche))
//
//    joueur1.ajouterPokemon(carapuce)
//    println(joueur1.equipe)
//    Combat(joueur1, adversaire1).lancerCombat()
}