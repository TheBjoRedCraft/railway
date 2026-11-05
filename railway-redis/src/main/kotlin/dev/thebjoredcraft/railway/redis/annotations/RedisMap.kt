package dev.thebjoredcraft.railway.redis.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RedisMap(val keyTemplate: String)