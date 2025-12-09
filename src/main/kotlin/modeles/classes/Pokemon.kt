package modeles.classes

import kotlinx.serialization.Serializable
import modeles.exceptions.CapaciteeException
import modeles.exceptions.NiveauException
import modeles.exceptions.PPException
import modeles.exceptions.PVException

@Serializable
data class Pokemon(
    val espece: EspecePokemon,
    var niveau: Int,
    var stats: Stats,
    var pvActuels: Int,
    var competences: MutableList<CapaciteeApprise>,
){
    companion object{
        fun creer(espece: EspecePokemon, niveau: Int, capaciteeDex: CapaciteeDex) : Pokemon{
            val stats = calculerStatsFinal(espece, niveau)

            val competences = espece.capacitesDeBase.map { idCapacitee ->
                val data = capaciteeDex.getParId(idCapacitee)
                CapaciteeApprise(
                    idCapacitee,
                    data.nom,
                    data.stats.pp,
                    data.stats.ppMax
                )
            }.toMutableList()

            return Pokemon(
                espece,
                niveau,
                stats,
                stats.pv,
                competences
            )
        }

        fun calculPV(espece : EspecePokemon, niveau: Int) : Int {
            return (((espece.baseStats.pv * 2) * niveau)/100 + niveau*1.5 + 10).toInt()
        }

        fun calculStats(niveau: Int, stat: Int): Int {
            return ((stat * 2) * niveau) / 100 + niveau/2 +  5
        }

        fun calculerStatsFinal(espece: EspecePokemon, niveau: Int) : Stats{
            val pv = calculPV(espece, niveau)
            val attaque = calculStats(niveau, espece.baseStats.attaque)
            val defense = calculStats(niveau, espece.baseStats.defense)
            val attaqueSpe = calculStats(niveau, espece.baseStats.attaqueSpe)
            val defenseSpe = calculStats(niveau, espece.baseStats.defenseSpe)
            val vitesse = calculStats(niveau, espece.baseStats.vitesse)

            return Stats(pv, attaque, defense, attaqueSpe, defenseSpe, vitesse)
        }
    }

    fun apprendreCapacitee(idCapacitee: Int, capaciteeDex: CapaciteeDex) : Boolean{
        if (competences.any { it.id == idCapacitee }){
            throw CapaciteeException("On ne peut pas apprendre deux fois la même capacitée")
        } else if (competences.size == 4){
            throw CapaciteeException("On ne peut pas avoir plus de 4 capacitées")
        }

        val data = capaciteeDex.getParId(idCapacitee)
        competences.add(
            CapaciteeApprise(
                idCapacitee,
                data.nom,
                data.stats.pp,
                data.stats.ppMax
            )
        )
        return true
    }

    fun oublierCapacitee(idCapacitee: Int) : Boolean{
        if (competences.size == 1){
            throw CapaciteeException("On est obligé de garder au moins une capacitée")
        }

        val index = competences.indexOfFirst { it.id == idCapacitee }
        if (index == -1){
            throw CapaciteeException("Le pokémon ne connait pas cette capacitée")
        }

        competences.removeAt(index)
        return true
    }

    fun monterDeNiveau() : Boolean{
        if (niveau == 100){
            throw NiveauException("On ne peut pas monter plus haut de niveau après 100")
        }

        niveau++
        stats = calculerStatsFinal(espece, niveau)
        pvActuels = stats.pv
        return true
    }

    fun subirDegats(degats: Int) : Int{
        if (pvActuels == 0){
            throw PVException("La vie du pokémon est déjà à 0")
        }
        if (pvActuels <= degats){
            pvActuels = 0
            return 0
        }
        pvActuels-=degats
        return pvActuels
    }

    fun soigner(pv: Int) : Int{
        if (pvActuels == stats.pv){
            throw PVException("La vie du pokémon ne peut pas être plus grand que ses stats")
        }
        if (pv >= stats.pv-pvActuels){
            pvActuels = stats.pv
            return pvActuels
        }
        pvActuels += pv
        return pvActuels
    }

    fun soinTotal() : Int{
        if (pvActuels == stats.pv){
            throw PVException("La vie du pokémon est déjà au maximum")
        }
        pvActuels = stats.pv
        return pvActuels
    }

    fun soinPP(pp : Int, idCapacitee: Int) : Int{
        if (competences[idCapacitee].ppActuels == competences[idCapacitee].ppMax){
            throw PPException("Le nombre de pp est déjà au maximum sur cette capacitée")
        }
        if (pp >= competences[idCapacitee].ppMax){
            competences[idCapacitee].ppActuels = competences[idCapacitee].ppMax
            return competences[idCapacitee].ppActuels
        }
        competences[idCapacitee].ppActuels+=pp
        return competences[idCapacitee].ppActuels
    }

    fun soinTotalPP(idCapacitee : Int) : Int{
        if (competences[idCapacitee].ppActuels == competences[idCapacitee].ppMax){
            throw PPException("Le nombre de pp est déjà au maximum sur cette capacitée")
        }
        competences[idCapacitee].ppActuels = competences[idCapacitee].ppMax
        return competences[idCapacitee].ppActuels
    }

    fun estKO(): Boolean{
        return pvActuels == 0
    }

}