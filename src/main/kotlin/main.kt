import modeles.classes.*
import modeles.enums.Capacitee

fun main() {
    val pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")

    val pikachuEspece = pokedex.especes.first { it.id == 25 }

    val niveau = 50
    val pikachu = Pokemon.creer(pikachuEspece, niveau)
    val pikachu2 = Pokemon.creer(pikachuEspece, 57)
    println(pikachu.pvActuels)
    pikachu.subirDegats(240)
    println(pikachu.pvActuels)
    println(pikachu.estKo)

    val joueur1 = Joueur.creer("jean")
    println(joueur1.nom)

    joueur1.ajouterPokemon(pikachu)
    joueur1.ajouterPokemon(pikachu2)
    println(joueur1.equipe)
    joueur1.supprimerPokemon(pikachu2)
    println(joueur1.equipe)
    joueur1.supprimerPokemon(pikachu)
    println(joueur1.equipe)

}