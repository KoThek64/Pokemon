import modeles.classes.*
import modeles.enums.Capacitee

fun main() {
    val pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")

    val pikachuEspece = pokedex.especes.first { it.id == 25 }

    val niveau = 50
    val pikachu = Pokemon.creer(pikachuEspece, niveau)
    pikachu.monterDeNiveau()

    println("Stats de Pikachu niveau ${pikachu.niveau} :")
    println(pikachu.stats)
    println(pikachu.competences)

    val pikachu2 = Pokemon.creer(pikachuEspece, niveau)
    println(pikachu2.stats)
    println(pikachu2.espece.types)

    val salamecheEspece = pokedex.especes.first { it.nom == "Salamèche" }
    val salameche = Pokemon.creer(salamecheEspece, niveau)
    println(salameche.stats)

    salameche.apprendreCapacitee(Capacitee.LANCE_FLAMME)
    println(salameche.competences)
    salameche.oublierCapacitee(Capacitee.LANCE_FLAMME)
    println(salameche.competences)
}