package dev.narcos.mapgen.cache

import dev.narcos.mapgen.cache.config.ObjectConfig

class ConfigArchive private constructor(
    val objects: ObjectConfig,
) {
    companion object {
        fun load(): ConfigArchive = ConfigArchive(
            ObjectConfig.load()
        )
    }
}