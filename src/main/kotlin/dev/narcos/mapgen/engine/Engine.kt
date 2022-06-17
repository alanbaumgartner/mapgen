package dev.narcos.mapgen.engine

import dev.narcos.mapgen.common.inject
import dev.narcos.mapgen.engine.model.world.World

class Engine {

    private val world: World by inject()

    fun start() {
        world.load()
    }

}