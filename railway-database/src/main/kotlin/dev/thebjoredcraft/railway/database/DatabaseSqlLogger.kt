package dev.thebjoredcraft.railway.database

import org.jetbrains.exposed.v1.core.SqlLogger
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementContext
import org.jetbrains.exposed.v1.core.statements.expandArgs
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

class DatabaseSqlLogger(val level: Level) : SqlLogger {
    private val baseLogger = LoggerFactory.getLogger("RailwayDatabase")

    override fun log(
        context: StatementContext,
        transaction: Transaction
    ) {
        baseLogger.atLevel(level).log("SQL: ${context.expandArgs(transaction)}")
    }
}