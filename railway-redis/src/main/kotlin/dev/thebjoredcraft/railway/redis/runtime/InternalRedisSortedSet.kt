package dev.thebjoredcraft.railway.redis.runtime

import dev.thebjoredcraft.railway.redis.api.RedisSortedSet
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.redisson.api.RScoredSortedSetReactive

internal class InternalRedisSortedSet<T>(
    private val sortedSet: RScoredSortedSetReactive<T>
) : RedisSortedSet<T> {
    override suspend fun add(score: Double, value: T): Boolean =
        sortedSet.add(score, value).awaitSingle()

    override suspend fun remove(value: T): Boolean = sortedSet.remove(value).awaitSingle()
    override suspend fun score(value: T): Double? = sortedSet.getScore(value).awaitSingleOrNull()
}