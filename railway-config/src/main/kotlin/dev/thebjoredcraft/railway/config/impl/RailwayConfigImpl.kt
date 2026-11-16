package dev.thebjoredcraft.railway.config.impl

import dev.thebjoredcraft.railway.config.RailwayConfig
import dev.thebjoredcraft.railway.config.RailwayConfigFormat
import dev.thebjoredcraft.railway.config.serializer.*
import org.spongepowered.configurate.BasicConfigurationNode
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import org.spongepowered.configurate.kotlin.dataClassFieldDiscoverer
import org.spongepowered.configurate.objectmapping.ObjectMapper
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicReference

private val mapperFactory =
    ObjectMapper.factoryBuilder().addDiscoverer(dataClassFieldDiscoverer()).build()
private val serializers = TypeSerializerCollection.builder()
    .registerAll(TypeSerializerCollection.defaults())
    .register(EnumValueSerializer)
    .register(FileSerializer)
    .register(PathSerializer)
    .register(PatternSerializer)
    .register(UriSerializer)
    .register(UrlSerializer)
    .register(UuidSerializer)
    .registerAnnotatedObjects(mapperFactory)
    .build()

internal class RailwayConfigImpl<T : Any>(
    override val path: Path,
    override val format: RailwayConfigFormat,
    private val defaultValue: T
) : RailwayConfig<T> {
    @Suppress("UNCHECKED_CAST")
    private val mapper = mapperFactory.get(defaultValue::class.java as Class<T>)
    private val state = AtomicReference(defaultValue)

    private val loadNode: () -> ConfigurationNode
    private val saveNode: (ConfigurationNode) -> Unit

    private val actualPath =
        if (path.toFile().isDirectory) path.resolve("config-${defaultValue.javaClass.simpleName.lowercase()}.yml") else path

    init {
        when (format) {
            RailwayConfigFormat.YAML -> {
                val loader = YamlConfigurationLoader.builder()
                    .path(actualPath)
                    .defaultOptions {
                        it.serializers(serializers)
                    }
                    .build()

                loadNode = { loader.load() }
                saveNode =
                    { loader.save(it as CommentedConfigurationNode) }
            }

            RailwayConfigFormat.JSON -> {
                val loader = GsonConfigurationLoader.builder()
                    .path(actualPath)
                    .defaultOptions { it.serializers(serializers) }
                    .build()

                loadNode = { loader.load() }
                saveNode =
                    { loader.save(it as BasicConfigurationNode) }
            }
        }
    }

    override val value: T
        get() = state.get()

    override fun load(): T {
        if (!Files.exists(actualPath)) {
            state.set(defaultValue)

            save()
            return defaultValue
        }

        val node = loadNode()
        val loaded = mapper.load(node)

        state.set(loaded)
        return loaded
    }

    override fun save() {
        val node = when (format) {
            RailwayConfigFormat.YAML -> CommentedConfigurationNode.root()
            RailwayConfigFormat.JSON -> BasicConfigurationNode.root()
        }

        mapper.save(value, node)
        saveNode(node)
    }

    override fun reload(): T = load()

    override fun update(transform: T.() -> T) {
        state.updateAndGet(transform)
    }
}