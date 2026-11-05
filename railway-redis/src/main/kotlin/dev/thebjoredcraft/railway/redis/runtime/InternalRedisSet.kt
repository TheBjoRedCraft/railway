package dev.thebjoredcraft.railway.redis.runtime

import dev.thebjoredcraft.railway.redis.api.RedisSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.redisson.api.RSetReactive

@OptIn(ExperimentalCoroutinesApi::class)
internal class InternalRedisSet<T>(
    private val set: RSetReactive<T>
) : RedisSet<T> {
    override suspend fun add(vararg values: T): Boolean =
        set.addAll(mutableListOf(*values)).awaitSingle()

    override suspend fun remove(vararg values: T): Boolean =
        set.removeAll(mutableListOf(*values)).awaitSingle()

    override suspend fun contains(value: T): Boolean = set.contains(value).awaitSingle()
    override fun members(): Flow<T> = set.readAll()
        .asFlow()
        .flatMapConcat { innerCollection -> innerCollection.asFlow() }

    override suspend fun size(): Long = set.size().awaitSingle().toLong()
}