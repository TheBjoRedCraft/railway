package dev.thebjoredcraft.railway.redis

import dev.thebjoredcraft.railway.config.RailwayConfig
import dev.thebjoredcraft.railway.config.RailwayConfigFormat
import dev.thebjoredcraft.railway.config.manager.ConfigManager
import dev.thebjoredcraft.railway.redis.config.RailwayRedisConfig
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import java.nio.file.Path

object RedisConnection {
    private var redisClient: RedissonClient? = null
    private lateinit var configHolder: RailwayConfig<RailwayRedisConfig>

    fun create(
        host: String,
        port: Int,
        password: String
    ) = create(
        RailwayRedisConfig(
            host = host,
            port = port,
            password = password
        )
    )

    fun create(path: Path) = create(
        ConfigManager.create(
            path = path.resolve("redis-config.yml"),
            format = RailwayConfigFormat.YAML,
            default = RailwayRedisConfig()
        ).also {
            configHolder = it
        }.load()
    )

    fun create(config: RailwayRedisConfig) {
        val redisConfig = Config()

        redisConfig.useSingleServer().setAddress("redis://${config.host}:${config.port}")
        redisConfig.password = config.password

        runCatching {
            redisClient = Redisson.create(redisConfig)
        }.onFailure {
            error("Failed to connect to the redis: ${it.message}")
        }
    }

    fun reconnect(config: RailwayRedisConfig = configHolder.reload()) {
        disconnect()
        create(config)
    }

    fun disconnect() {
        redisClient?.shutdown()
        redisClient = null
    }
}