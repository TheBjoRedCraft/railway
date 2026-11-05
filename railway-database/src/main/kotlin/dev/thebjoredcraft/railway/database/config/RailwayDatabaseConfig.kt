package dev.thebjoredcraft.railway.database.config

import dev.thebjoredcraft.railway.database.type.DatabaseType
import org.slf4j.event.Level
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@ConfigSerializable
data class RailwayDatabaseConfig(
    val host: String = "localhost",
    val port: Int = 5432,
    val username: String = "postgres",
    val password: String = "password",
    val databaseName: String = "railway",
    val type: DatabaseType = DatabaseType.POSTGRESQL,
    val logLevel: Level = Level.DEBUG,
    val pool: RailwayDatabasePoolConfig = RailwayDatabasePoolConfig()
) {
    @ConfigSerializable
    data class RailwayDatabasePoolConfig(
        val maxSize: Int = 10,
        val initialSize: Int = 10,
        val minIdle: Int = 0,
        val maxCreateConnectionTimeMillis: Long = 30.seconds.inWholeMilliseconds,
        val maxAcquireTimeMillis: Long = 10.seconds.inWholeMilliseconds,
        val maxIdleTimeMillis: Long = 60.seconds.inWholeMilliseconds,
        val maxValidationTimeMillis: Long = -1,
        val maxLifeTimeMillis: Long = 30.minutes.inWholeMilliseconds
    )
}
