package dev.thebjoredcraft.railway.redis.api

interface RedisQueue<T> {
    suspend fun add(value: T): Boolean
    suspend fun poll(): T?
    suspend fun peek(): T?
    suspend fun size(): Int
}
