package dev.narcos.mapgen.engine

import dev.narcos.mapgen.cache.GameCache
import dev.narcos.mapgen.engine.model.world.World
import org.koin.dsl.module

val ENGINE_MODULE = module {
    single { Engine() }
    single { World() }
    single { GameCache() }
}