package dev.thebjoredcraft.railway.redis.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RedisSortedSet(val keyTemplate: String)