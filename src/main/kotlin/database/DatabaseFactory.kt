package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.Properties

object DatabaseFactory {
    fun init() {
        val config = loadConfig()

        val host = config.getProperty("db.host")
        val port = config.getProperty("db.port")
        val dbName = config.getProperty("db.name")
        val user = config.getProperty("db.user")
        val password = config.getProperty("db.password")

        val url = "jdbc:postgresql://$host:$port/$dbName"
        val driver = "org.postgresql.Driver"

        Database.connect(url, driver, user, password)

        transaction {
            SchemaUtils.create(PokemonTable, CapaciteeTable, PokemonCapacitesTable)
            println("✅ Connexion PostgreSQL établie et tables synchronisées.")
        }
    }

    private fun loadConfig(): Properties {
        val props = Properties()
        val configFile = File("db.properties")

        if (!configFile.exists()) {
            throw IllegalStateException("""
                Fichier db.properties introuvable !
                Créez-le à la racine du projet avec :

                db.host=<IP_TAILSCALE>
                db.port=5432
                db.name=pokemon_db
                db.user=<USER>
                db.password=<PASSWORD>
            """.trimIndent())
        }

        configFile.inputStream().use { props.load(it) }
        return props
    }
}