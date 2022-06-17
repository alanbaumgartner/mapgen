package dev.narcos.mapgen

import org.koin.core.context.startKoin
import dev.narcos.mapgen.cache.GameCache
import dev.narcos.mapgen.common.inject
import dev.narcos.mapgen.config.XteaConfig
import dev.narcos.mapgen.engine.Engine
import java.io.File

object Launcher {

    private val engine: Engine by inject()
    private val cache: GameCache by inject()

    @JvmStatic
    fun main(args: Array<String>) {
        init()
        launch()
    }

    private fun init() {

        startKoin { modules(DI_MODULES) }

        /*
         * Execute all initialization logics.
         */
        checkDirs()
        loadConfigs()
        loadCache()
    }

    private fun launch() {
        engine.start()
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

    private fun loadConfigs() {
        XteaConfig.load()
    }

    private fun loadCache() {
        cache.load(File("data/cache/"))
    }

}