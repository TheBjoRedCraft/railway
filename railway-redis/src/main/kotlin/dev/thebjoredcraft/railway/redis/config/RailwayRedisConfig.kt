package dev.thebjoredcraft.railway.redis.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class RailwayRedisConfig(
    val host: String = "localhost",
    val port: Int = 6379,
    val password: String? = null
)
