package dev.thebjoredcraft.railway.config.manager

import dev.thebjoredcraft.railway.config.RailwayConfig
import dev.thebjoredcraft.railway.config.RailwayConfigFormat
import dev.thebjoredcraft.railway.config.impl.ConfigurateConfig
import java.nio.file.Path

object ConfigManager {
    fun <T : Any> create(
        path: Path,
        format: RailwayConfigFormat,
        default: T
    ): RailwayConfig<T> {
        return ConfigurateConfig(path, format, default)
    }
}