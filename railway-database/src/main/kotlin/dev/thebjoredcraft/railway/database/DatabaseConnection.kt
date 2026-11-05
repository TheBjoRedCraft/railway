package dev.thebjoredcraft.railway.database

import dev.thebjoredcraft.railway.config.RailwayConfig
import dev.thebjoredcraft.railway.config.RailwayConfigFormat
import dev.thebjoredcraft.railway.config.manager.ConfigManager
import dev.thebjoredcraft.railway.database.config.RailwayDatabaseConfig
import dev.thebjoredcraft.railway.database.type.DatabaseType
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.jetbrains.exposed.v1.core.vendors.MariaDBDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.jetbrains.exposed.v1.r2dbc.transactions.TransactionManager
import org.mariadb.r2dbc.MariadbConnectionConfiguration
import org.mariadb.r2dbc.MariadbConnectionFactory
import java.nio.file.Path
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

object DatabaseConnection {
    private var database: R2dbcDatabase? = null
    private lateinit var configHolder: RailwayConfig<RailwayDatabaseConfig>

    fun create(
        type: DatabaseType,
        host: String,
        post: Int,
        username: String,
        password: String,
        databaseName: String,
    ) = create(
        RailwayDatabaseConfig(
            host = host,
            port = post,
            username = username,
            password = password,
            databaseName = databaseName,
            type = type
        )
    )

    fun create(path: Path) = create(
        ConfigManager.create(
            path = path.resolve("railway-database.yml"),
            format = RailwayConfigFormat.YAML,
            default = RailwayDatabaseConfig()
        ).also {
            configHolder = it
        }.load()
    )

    fun create(config: RailwayDatabaseConfig) {
        val connectionBuilder = when (config.type) {
            DatabaseType.POSTGRESQL -> MariadbConnectionFactory.from(
                MariadbConnectionConfiguration.builder()
                    .host(config.host)
                    .port(config.port)
                    .username(config.username)
                    .password(config.password)
                    .database(config.databaseName)
                    .build()
            )

            DatabaseType.MARIADB -> PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                    .host(config.host)
                    .port(config.port)
                    .username(config.username)
                    .password(config.password)
                    .database(config.databaseName)
                    .build()
            )
        }

        val connectionPool = ConnectionPool(
            ConnectionPoolConfiguration.builder()
                .connectionFactory(connectionBuilder)
                .initialSize(config.pool.initialSize)

                .maxSize(config.pool.maxSize)
                .minIdle(config.pool.minIdle)
                .maxIdleTime(config.pool.maxIdleTimeMillis.milliseconds.toJavaDuration())
                .maxLifeTime(config.pool.maxLifeTimeMillis.milliseconds.toJavaDuration())

                .maxCreateConnectionTime(config.pool.maxCreateConnectionTimeMillis.milliseconds.toJavaDuration())
                .maxAcquireTime(config.pool.maxAcquireTimeMillis.milliseconds.toJavaDuration())
                .maxValidationTime(config.pool.maxValidationTimeMillis.milliseconds.toJavaDuration())
                .build()
        )

        runCatching {
            database = R2dbcDatabase.connect(
                connectionPool,
                databaseConfig = R2dbcDatabaseConfig {
                    explicitDialect = when (config.type) {
                        DatabaseType.POSTGRESQL -> PostgreSQLDialect()
                        DatabaseType.MARIADB -> MariaDBDialect()
                    }
                    sqlLogger = DatabaseSqlLogger(config.logLevel)
                }
            )
        }.onFailure {
            error("Failed to connect to the database: ${it.message}")
        }
    }

    fun reconnect(config: RailwayDatabaseConfig = configHolder.reload()) {
        disconnect()
        create(config)
    }

    fun disconnect() {
        val db = database ?: return

        TransactionManager.closeAndUnregister(db)
        database = null
    }
}