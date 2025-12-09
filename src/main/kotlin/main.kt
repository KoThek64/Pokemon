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
    val salameche = Pokemon.creer(pokedex.trouverEspeceParNom("Salamèche"), 50, capaciteeDex)
    val carapuce = Pokemon.creer(pokedex.trouverEspeceParNom("Carapuce"), 50, capaciteeDex)
    val bulbizarre = Pokemon.creer(pokedex.trouverEspeceParNom("Bulbizarre"), 50, capaciteeDex)

    val joueur1 = Joueur.creer("Mattys")
    val adversaire1 = Adversaire("claude", mutableListOf(salameche, bulbizarre))

    joueur1.ajouterPokemon(carapuce)
    joueur1.ajouterPokemon(pikachu)

    Combat(joueur1, adversaire1, capaciteeDex).lancerCombat()
}