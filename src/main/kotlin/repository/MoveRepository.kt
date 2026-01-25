package repository

import database.CapaciteeTable
import modeles.classes.CapaciteeData
import modeles.classes.StatsCapacitee
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object MoveRepository {

    fun getParId(id: Int): CapaciteeData = transaction {
        CapaciteeTable.select { CapaciteeTable.id eq id }
            .map { rowToMove(it) }
            .singleOrNull() ?: error("Capacité ID $id introuvable sur le Cuby")
    }

    fun all() : List<CapaciteeData> = transaction {
        CapaciteeTable.selectAll().map { rowToMove(it) }
    }

    private fun rowToMove(row : ResultRow) : CapaciteeData{
        return CapaciteeData(
            row[CapaciteeTable.id],
            row[CapaciteeTable.nom],
            row[CapaciteeTable.categorie],
            StatsCapacitee(
                row[CapaciteeTable.puissance],
                row[CapaciteeTable.precision],
                row[CapaciteeTable.pp],
                row[CapaciteeTable.pp]
            ),
            row[CapaciteeTable.type]
        )
    }
}
