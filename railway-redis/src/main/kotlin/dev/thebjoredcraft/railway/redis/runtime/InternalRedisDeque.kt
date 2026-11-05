package dev.thebjoredcraft.railway.redis.runtime

import dev.thebjoredcraft.railway.redis.api.RedisDeque
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.redisson.api.RDequeReactive

internal class InternalRedisDeque<T>(
    private val deque: RDequeReactive<T>
) : RedisDeque<T> {
    override suspend fun addFirst(value: T) {
        deque.addFirst(value).awaitSingle()
    }

    override suspend fun addLast(value: T) {
        deque.addLast(value).awaitSingle()
    }

    override suspend fun pollFirst(): T? = deque.pollFirst().awaitSingleOrNull()
    override suspend fun pollLast(): T? = deque.pollLast().awaitSingleOrNull()
    override suspend fun size(): Int = deque.size().awaitSingle()
}