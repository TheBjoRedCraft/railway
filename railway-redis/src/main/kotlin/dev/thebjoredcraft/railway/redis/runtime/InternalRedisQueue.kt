package dev.thebjoredcraft.railway.redis.runtime

import dev.thebjoredcraft.railway.redis.api.RedisQueue
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.redisson.api.RQueueReactive

internal class InternalRedisQueue<T>(
    private val queue: RQueueReactive<T>
) : RedisQueue<T> {
    override suspend fun add(value: T): Boolean = queue.add(value).awaitSingle()
    override suspend fun poll(): T? = queue.poll().awaitSingleOrNull()
    override suspend fun peek(): T? = queue.peek().awaitSingleOrNull()
    override suspend fun size(): Int = queue.size().awaitSingle()
}
