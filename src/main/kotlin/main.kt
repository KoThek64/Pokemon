import modeles.classes.*
import modeles.enums.Capacitee
import modeles.enums.Type

fun main() {
    val pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")

    val pikachuEspece = pokedex.especes.first { it.id == 25 }

    val niveau = 50
    val pikachu = Pokemon.creer(pikachuEspece, niveau)
    val pikachu2 = Pokemon.creer(pikachuEspece, 57)
    println(pikachu.pvActuels)
    pikachu.subirDegats(240)
    println(pikachu.pvActuels)
    println(pikachu.estKO())

    val joueur1 = Joueur.creer("jean")
    println(joueur1.nom)

    joueur1.ajouterPokemon(pikachu)
    joueur1.ajouterPokemon(pikachu2)
    println(joueur1.equipe)
    joueur1.supprimerPokemon(pikachu2)
    println(joueur1.equipe)

    println(joueur1.aEncoreDesPokemon())
    println(joueur1.getPokemonActif())

    joueur1.ajouterPokemon(pikachu2)
    println(joueur1.changerPokemonActif(1))
    println(joueur1.getPokemonActif())

    println(pokedex.trouverEspeceParID(25))
    println(pokedex.trouverEspeceParNom("Salamèche"))
    println(pokedex.rechercherParType(Type.EAU))

}