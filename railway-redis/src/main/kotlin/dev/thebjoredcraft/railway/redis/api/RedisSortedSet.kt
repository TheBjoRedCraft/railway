package dev.thebjoredcraft.railway.redis.api

interface RedisSortedSet<T> {
    suspend fun add(score: Double, value: T): Boolean
    suspend fun remove(value: T): Boolean
    suspend fun score(value: T): Double?
}