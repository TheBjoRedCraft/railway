package dev.thebjoredcraft.railway.redis.api

interface RedisDeque<T> {
    suspend fun addFirst(value: T)
    suspend fun addLast(value: T)
    suspend fun pollFirst(): T?
    suspend fun pollLast(): T?
    suspend fun size(): Int
}