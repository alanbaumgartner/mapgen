package dev.narcos.mapgen.engine

import dev.narcos.mapgen.cache.GameCache
import dev.narcos.mapgen.config.XteaConfig
import dev.narcos.mapgen.engine.model.world.World
import java.io.File

class Engine {

    private val cache = GameCache()
    private val world: World = World(cache)

    fun start(path: String) {
        checkDirs()
        XteaConfig.load()
        cache.load(File(path))
        world.load()
    }

    fun dumpCollisionMap(): ByteArray {
        return world.dumpCollisionMap()
    }

    private fun checkDirs() {
        listOf(
            "data/",
            "data/cache/",
        ).map { File(it) }.forEach { dir ->
            if(!dir.exists()) {
                dir.mkdirs()
            }
        }
    }

}