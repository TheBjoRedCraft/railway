package dev.thebjoredcraft.railway.redis.api

import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface RedisMap<K, V> {
    suspend fun get(key: K): V?
    suspend fun put(key: K, value: V): V?
    suspend fun remove(key: K): V?
    suspend fun containsKey(key: K): Boolean
    suspend fun size(): Int
    fun entries(): Flow<Pair<K, V>>
    suspend fun expire(ttl: Duration): Boolean
}