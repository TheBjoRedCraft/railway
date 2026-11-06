package dev.thebjoredcraft.railway.database.table.column

import org.jetbrains.exposed.v1.core.*
import java.sql.Date
import java.sql.Timestamp
import java.time.*

class OffsetDateTimeColumnType : ColumnType<OffsetDateTime>() {
    override fun sqlType(): String = "TIMESTAMP WITH TIME ZONE"

    override fun valueFromDB(value: Any): OffsetDateTime? = when (value) {
        is OffsetDateTime -> value
        is Instant -> value.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        is LocalDateTime -> value.atOffset(ZoneOffset.UTC)
        is Timestamp -> value.toInstant().atOffset(ZoneOffset.UTC)
        is Date -> value.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC)
        is String -> OffsetDateTime.parse(value)
        else -> error("Unexpected value of type OffsetDateTime: $value of ${value::class.qualifiedName}")
    }

    override fun notNullValueToDB(value: OffsetDateTime): Any = value.toInstant()

    companion object {
        internal val INSTANCE = OffsetDateTimeColumnType()
    }
}

fun Table.offsetDateTime(name: String): Column<OffsetDateTime> =
    registerColumn(name, OffsetDateTimeColumnType())

class CurrentOffsetDateTime : ExpressionWithColumnType<OffsetDateTime>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append("CURRENT_TIMESTAMP")
    }

    override val columnType: ColumnType<OffsetDateTime> = OffsetDateTimeColumnType.INSTANCE

    companion object {
        val WithUpdate = CurrentOffsetDateTime()
        val WithoutUpdate = CurrentOffsetDateTime()
    }
}
