package modeles.classes

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
class CapaciteeDex(
    private val capacitees: List<CapaciteeData>
) {
    companion object{
        fun chargerDepuisFichier(path: String): CapaciteeDex{
            val json = File(path).readText()
            val liste = Json.decodeFromString<List<CapaciteeData>>(json)
            return CapaciteeDex(liste)
        }
    }

    private val mapParId = capacitees.associateBy { it.id }

    fun getParId(id : Int): CapaciteeData{
        return mapParId[id] ?: error("Capacitee avec l'$id introuvable dans le JSON")
    }

    fun all() : List<CapaciteeData> = capacitees
}