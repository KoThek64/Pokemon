package modeles.classes

import modeles.enums.Capacitee
import kotlinx.serialization.Serializable
import modeles.exceptions.CapaciteeException

@Serializable
data class Pokemon(
    val espece: EspecePokemon,
    var niveau: Int,
    var stats: Stats,
    var pvActuels: Int,
    var competences: MutableList<Capacitee>
){
    companion object{
        fun creer(espece: EspecePokemon, niveau: Int) : Pokemon{
            val stats = calculerStatsFinal(espece, niveau)
            return Pokemon(
                espece,
                niveau,
                stats,
                stats.pv,
                espece.capacitesDeBase
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

    fun apprendreCapacitee(capacitee: Capacitee) : Boolean{
        if (competences.contains(capacitee)){
            throw CapaciteeException("On ne peut pas apprendre deux fois la même capacitée")
        } else if (competences.size == 4){
            throw CapaciteeException("On ne peut pas avoir plus de 4 capacitées")
        }
        competences.add(capacitee)
        return true
    }

    fun oublierCapacitee(capacitee: Capacitee) : Boolean{
        if (!competences.contains(capacitee)){
            throw CapaciteeException("On ne peut pas oublier une capacitée qui n'existe pas")
        }
        if (competences.size == 1){
            throw CapaciteeException("On est obligé de garder au moins une capacitée")
        }

        competences.remove(capacitee)
        return true
    }

    fun monterDeNiveau() : Boolean{
        if (niveau == 100){
            return false
        }

        niveau++
        stats = calculerStatsFinal(espece, niveau)
        pvActuels = stats.pv
        return true
    }
}