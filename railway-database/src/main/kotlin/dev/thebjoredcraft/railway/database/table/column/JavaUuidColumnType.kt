package dev.thebjoredcraft.railway.database.table.column

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.Table
import java.nio.ByteBuffer
import java.util.*

private val uuidRegexp =
    Regex(
        "[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}",
        RegexOption.IGNORE_CASE
    )

class JavaUuidColumnType : ColumnType<UUID>() {
    override fun sqlType() = "UUID"

    override fun valueFromDB(value: Any): UUID? = when (value) {
        is UUID -> value
        is String if value.matches(uuidRegexp) -> UUID.fromString(value)
        is String -> ByteBuffer.wrap(value.toByteArray()).let { UUID(it.long, it.long) }
        is ByteArray -> ByteBuffer.wrap(value).let { UUID(it.long, it.long) }
        is ByteBuffer -> UUID(value.long, value.long)
        else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
    }
}

fun Table.stringUuid(name: String): Column<UUID> = registerColumn(name, JavaUuidColumnType())