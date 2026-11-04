package dev.thebjoredcraft.railway.database

import dev.thebjoredcraft.railway.database.type.DatabaseType
import org.jetbrains.exposed.v1.core.vendors.MariaDBDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.jetbrains.exposed.v1.r2dbc.transactions.TransactionManager

object DatabaseConnection {
    private var database: R2dbcDatabase? = null

    fun create(
        type: DatabaseType,
        host: String,
        post: Int,
        username: String,
        password: String,
        databaseName: String
    ) {
        val driver = when (type) {
            DatabaseType.POSTGRESQL -> "org.postgresql.Driver"
            DatabaseType.MARIADB -> "org.mariadb.jdbc.Driver"
        }

        database = R2dbcDatabase.connect(
            url = "r2dbc:${type.name.lowercase()}://$host:$post/$databaseName",
            driver = driver,
            user = username,
            password = password,
            databaseConfig = R2dbcDatabaseConfig {
                explicitDialect = when (type) {
                    DatabaseType.POSTGRESQL -> PostgreSQLDialect()
                    DatabaseType.MARIADB -> MariaDBDialect()
                }
            }
        )
    }

    fun disconnect() {
        val db = database ?: return

        TransactionManager.closeAndUnregister(db)
        database = null
    }
}