package dev.thebjoredcraft.railway.database.table.auditable

import dev.thebjoredcraft.railway.database.table.PostgreSQLUpdateHook
import dev.thebjoredcraft.railway.database.table.column.CurrentOffsetDateTime
import dev.thebjoredcraft.railway.database.table.column.offsetDateTime
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

open class AuditableLongIdTable(name: String) : LongIdTable(name) {
    val createdAt = offsetDateTime("created_at")
        .defaultExpression(CurrentOffsetDateTime.WithoutUpdate)
    val updatedAt = offsetDateTime("updated_at")
        .defaultExpression(CurrentOffsetDateTime.WithUpdate)

    override fun createStatement(): List<String> {
        val statements = super.createStatement()
        return PostgreSQLUpdateHook.modifyStatements(statements, this, updatedAt, tableName)
    }
}
