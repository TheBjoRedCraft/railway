package dev.thebjoredcraft.railway.config

import java.nio.file.Path

interface RailwayConfig<T : Any> {
    val path: Path
    val format: RailwayConfigFormat
    val value: T

    fun load(): T
    fun save()
    fun reload(): T
    fun update(transform: T.() -> T)
}