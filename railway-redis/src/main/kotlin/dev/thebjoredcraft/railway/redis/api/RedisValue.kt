package dev.thebjoredcraft.railway.redis.api

import java.time.Duration

interface RedisValue<T> {
    suspend fun get(): T?
    suspend fun set(value: T, ttl: Duration? = null)
    suspend fun getAndSet(value: T): T?
    suspend fun delete(): Boolean
    suspend fun expire(ttl: Duration): Boolean
    suspend fun ttl(): Duration?
}
