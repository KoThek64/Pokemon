package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        // --- CONFIGURATION TAILSCALE ---
        val tailscaleIp = "100.84.207.1"
        val dbName = "pokemon_db"
        val user = "trainer"
        val password = "123456"

        val url = "jdbc:postgresql://$tailscaleIp:5432/$dbName"
        val driver = "org.postgresql.Driver"

        // Connexion au Cuby
        Database.connect(url, driver, user, password)

        // Création automatique des tables au premier lancement
        transaction {
            SchemaUtils.create(PokemonTable, CapaciteeTable, PokemonCapacitesTable)
            println("✅ Connexion PostgreSQL établie et tables synchronisées sur Cuby.")
        }
    }
}
