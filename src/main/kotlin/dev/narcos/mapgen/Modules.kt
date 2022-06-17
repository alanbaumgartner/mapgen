package dev.narcos.mapgen

import dev.narcos.mapgen.config.CONFIG_MODULE
import dev.narcos.mapgen.engine.ENGINE_MODULE

val DI_MODULES = listOf(
    ENGINE_MODULE,
    CONFIG_MODULE
)