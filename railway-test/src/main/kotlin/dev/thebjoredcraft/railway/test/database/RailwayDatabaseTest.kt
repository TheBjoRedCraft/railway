package dev.thebjoredcraft.railway.test.database

import dev.thebjoredcraft.railway.database.DatabaseConnection
import dev.thebjoredcraft.railway.database.type.DatabaseType
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import java.util.*

suspend fun main() {
    DatabaseConnection.create(
        DatabaseType.POSTGRESQL,
        "localhost",
        5432,
        "railway",
        "railway",
        "railway"
    )


    println("Connected to database.")
    println("Creating tables...")

    suspendTransaction {
        SchemaUtils.create(RailwayDatabaseTestTable)
    }

    println("Tables created.")
    println("Inserting data...")

    suspendTransaction {
        RailwayDatabaseTestTable.insert {
            it[userUuid] = UUID.randomUUID()
            it[username] = "testuser"
            it[email] = "test@test.com"
            it[isActive] = true
        }
    }

    println("Inserted test data into database.")

    val users = suspendTransaction {
        RailwayDatabaseTestTable.selectAll()
            .map { "User: ${it[RailwayDatabaseTestTable.username]} - ${it[RailwayDatabaseTestTable.email]}" }
            .toList()
    }

    println("Retrieved users from database: $users")

    println("Disconnecting from database.")
    DatabaseConnection.disconnect()
    println("Disconnected from database.")
}