package dev.thebjoredcraft.railway.redis.runtime

import dev.thebjoredcraft.railway.redis.api.RedisMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.redisson.api.RMapReactive
import java.time.Duration

internal class InternalRedisMap<K, V>(
    private val map: RMapReactive<K, V>
) : RedisMap<K, V> {
    override suspend fun get(key: K): V? = map.get(key).awaitSingleOrNull()
    override suspend fun put(key: K, value: V): V? = map.put(key, value).awaitSingleOrNull()
    override suspend fun remove(key: K): V? = map.remove(key).awaitSingleOrNull()
    override suspend fun containsKey(key: K): Boolean = map.containsKey(key).awaitSingle()
    override suspend fun size(): Int = map.size().awaitSingle()
    override fun entries(): Flow<Pair<K, V>> =
        map.entryIterator().asFlow().map { entry -> entry.key to entry.value }

    override suspend fun expire(ttl: Duration): Boolean = map.expire(ttl).awaitSingle()
}