package dev.thebjoredcraft.railway.redis.api

import kotlinx.coroutines.flow.Flow

interface RedisSet<T> {
    suspend fun add(vararg values: T): Boolean
    suspend fun remove(vararg values: T): Boolean
    suspend fun contains(value: T): Boolean
    fun members(): Flow<T>
    suspend fun size(): Long
}