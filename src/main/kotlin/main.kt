import kotlinx.coroutines.runBlocking
import modeles.classes.*
import repository.MoveRepository
import repository.PokemonRepository
import sauvegarde.SauvegardeService
import service.MoveSeeder
import service.PokemonSeeder

fun main() = runBlocking {
    database.DatabaseFactory.init()
    initBD()

    try {
        lancerJeu()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun lancerJeu() {
    val joueur = gererSauvegarde()

    // Créer un adversaire (depuis la BD)
    val salameche = creerPokemonDepuisBD("Salamèche", 5)
    val bulbizarre = creerPokemonDepuisBD("Bulbizarre", 5)
    val adversaire = Adversaire("Benoit", mutableListOf(salameche, bulbizarre))

    // Lancer le combat
    Combat(joueur, adversaire).lancerCombat()

    // Sauvegarder après le combat
    SauvegardeService.sauvegarder(joueur)
    println("\nPartie sauvegardée !")
}

fun gererSauvegarde(): Joueur {
    if (SauvegardeService.sauvegardeExiste()) {
        println("Une sauvegarde existe. Voulez-vous la charger ? (o/n)")

        when (readln().lowercase()) {
            "o", "oui" -> {
                val joueur = SauvegardeService.charger()
                if (joueur != null) {
                    println("Bienvenue, ${joueur.nom} !")
                    println("Votre équipe :")
                    joueur.equipe.forEachIndexed { i, p ->
                        println("  ${i + 1}. ${p.espece.nom} (Niv.${p.niveau}) - ${p.pvActuels}/${p.stats.pv} PV")
                    }
                    return joueur
                }
                println("Erreur lors du chargement. Nouvelle partie...")
            }
            else -> {
                println("Nouvelle partie...")
                Joueur.reset()
                SauvegardeService.supprimerSauvegarde()
            }
        }
    }

    return creerNouvellePartie()
}

fun creerNouvellePartie(): Joueur {
    println("Entrez votre nom :")
    val nom = readln()

    val joueur = Joueur.creer(nom)

    // Donner des Pokémon de départ (depuis la BD)
    val starter1 = creerPokemonDepuisBD("Pikachu", 5)
    val starter2 = creerPokemonDepuisBD("Carapuce", 5)

    joueur.ajouterPokemon(starter1)
    joueur.ajouterPokemon(starter2)

    println("Vous avez reçu ${starter1.espece.nom} et ${starter2.espece.nom} !")

    return joueur
}

fun creerPokemonDepuisBD(nomEspece: String, niveau: Int): Pokemon {
    val espece = PokemonRepository.trouverEspeceParNom(nomEspece)
    val stats = Pokemon.calculerStatsFinal(espece, niveau)

    val competences = espece.capacitesDeBase.take(4).map { idCapacitee ->
        val moveData = MoveRepository.getParId(idCapacitee)
        CapaciteeApprise(
            id = idCapacitee,
            nom = moveData.nom,
            ppActuels = moveData.stats.pp,
            ppMax = moveData.stats.ppMax
        )
    }.toMutableList()

    return Pokemon(espece, niveau, stats, stats.pv, competences)
}

suspend fun initBD(seedPokemon: Boolean = true, seedMoves: Boolean = true) {
    if (seedPokemon) PokemonSeeder.seedPokemon(1..151)
    if (seedMoves) MoveSeeder.seedAll(1..151)
}
