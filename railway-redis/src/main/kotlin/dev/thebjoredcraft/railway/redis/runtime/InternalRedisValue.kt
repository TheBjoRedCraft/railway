package dev.thebjoredcraft.railway.redis.runtime

import dev.thebjoredcraft.railway.redis.api.RedisValue
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.redisson.api.RBucketReactive
import java.time.Duration

internal class InternalRedisValue<T>(
    private val bucket: RBucketReactive<T>
) : RedisValue<T> {
    override suspend fun get(): T? = bucket.get().awaitSingleOrNull()
    override suspend fun set(value: T, ttl: Duration?) {
        if (ttl != null) bucket.set(value, ttl).awaitSingle() else bucket.set(value).awaitSingle()
    }

    override suspend fun getAndSet(value: T): T? = bucket.getAndSet(value).awaitSingleOrNull()
    override suspend fun delete(): Boolean = bucket.delete().awaitSingle()
    override suspend fun expire(ttl: Duration): Boolean = bucket.expire(ttl).awaitSingle()
    override suspend fun ttl(): Duration? {
        val millis = bucket.remainTimeToLive().awaitSingle()
        return if (millis < 0) null else Duration.ofMillis(millis)
    }
}