package dev.thebjoredcraft.railway.redis.serialization

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.redisson.client.codec.BaseCodec
import org.redisson.client.protocol.Decoder
import org.redisson.client.protocol.Encoder
import java.nio.charset.StandardCharsets

@Suppress("UNCHECKED_CAST")
internal class RailwayCodec<T>(
    private val serializer: KSerializer<T>,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : BaseCodec() {

    private val encoder = Encoder { value: Any? ->
        val output = ByteBufOutputStream(Unpooled.buffer())
        val string = json.encodeToString(serializer, value as T)
        output.write(string.toByteArray(StandardCharsets.UTF_8))
        output.buffer()
    }

    private val decoder = Decoder<Any?> { buf: ByteBuf, _ ->
        val bytes = ByteArray(buf.readableBytes())
        buf.readBytes(bytes)
        val string = String(bytes, StandardCharsets.UTF_8)
        json.decodeFromString(serializer, string)
    }

    override fun getValueEncoder(): Encoder = encoder
    override fun getValueDecoder(): Decoder<Any?> = decoder
}