package dev.thebjoredcraft.railway.test.database

import dev.thebjoredcraft.railway.database.table.column.stringUuid
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object RailwayDatabaseTestTable : LongIdTable("railway_test_table") {
    val userUuid = stringUuid("user_uuid")
    val username = varchar("username", 255)
    val email = varchar("email", 255)
    val isActive = bool("is_active").default(true)
}