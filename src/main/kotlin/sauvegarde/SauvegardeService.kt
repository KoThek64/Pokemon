package sauvegarde

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import modeles.classes.*
import repository.MoveRepository
import repository.PokemonRepository
import sauvegarde.dto.*
import java.io.File

object SauvegardeService {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private const val SAVE_DIR = "saves"
    private const val SAVE_FILE = "joueur.json"

    // ============ SAUVEGARDE ============

    fun sauvegarder(joueur: Joueur) {
        val saveData = joueurToSaveData(joueur)
        val jsonString = json.encodeToString(saveData)

        File(SAVE_DIR).mkdirs()
        File("$SAVE_DIR/$SAVE_FILE").writeText(jsonString)
    }

    private fun joueurToSaveData(joueur: Joueur): JoueurSaveData {
        return JoueurSaveData(
            nom = joueur.nom,
            argent = joueur.argent,
            equipe = joueur.equipe.map { pokemonToSaveData(it) }
        )
    }

    private fun pokemonToSaveData(pokemon: Pokemon): PokemonSaveData {
        return PokemonSaveData(
            especeId = pokemon.espece.id,
            niveau = pokemon.niveau,
            pvActuels = pokemon.pvActuels,
            competences = pokemon.competences.map {
                CapaciteeSaveData(it.id, it.ppActuels)
            }
        )
    }

    // ============ CHARGEMENT ============

    fun charger(): Joueur? {
        val file = File("$SAVE_DIR/$SAVE_FILE")
        if (!file.exists()) return null

        return try {
            val jsonString = file.readText()
            val saveData = json.decodeFromString<JoueurSaveData>(jsonString)
            reconstruireJoueur(saveData)
        } catch (e: Exception) {
            println("Erreur lors du chargement de la sauvegarde: ${e.message}")
            null
        }
    }

    fun sauvegardeExiste(): Boolean {
        return File("$SAVE_DIR/$SAVE_FILE").exists()
    }

    fun supprimerSauvegarde() {
        File("$SAVE_DIR/$SAVE_FILE").delete()
    }

    // ============ RECONSTRUCTION ============

    private fun reconstruireJoueur(data: JoueurSaveData): Joueur {
        val equipe = data.equipe.map { reconstruirePokemon(it) }.toMutableList()
        return Joueur.chargerDepuisSauvegarde(data.nom, equipe, data.argent)
    }

    private fun reconstruirePokemon(data: PokemonSaveData): Pokemon {
        val espece = PokemonRepository.trouverEspeceParID(data.especeId)
        val stats = Pokemon.calculerStatsFinal(espece, data.niveau)

        val competences = data.competences.map { capData ->
            val moveData = MoveRepository.getParId(capData.id)
            CapaciteeApprise(
                id = capData.id,
                nom = moveData.nom,
                ppActuels = capData.ppActuels,
                ppMax = moveData.stats.ppMax
            )
        }.toMutableList()

        return Pokemon(
            espece = espece,
            niveau = data.niveau,
            stats = stats,
            pvActuels = data.pvActuels,
            competences = competences
        )
    }
}
