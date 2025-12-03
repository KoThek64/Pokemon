import modeles.classes.*
import modeles.enums.Capacitee

fun main() {
    val pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")

    val pikachuEspece = pokedex.especes.first { it.id == 25 }

    val niveau = 50
    val pikachu = Pokemon.creer(pikachuEspece, niveau)
    println(pikachu.pvActuels)
    pikachu.subirDegats(240)
    println(pikachu.pvActuels)
    println(pikachu.estKo)
    pikachu.soinTotal()
    println(pikachu.pvActuels)
    println(pikachu.estKo)
}