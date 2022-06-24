package dev.narcos.mapgen.cache

import net.runelite.cache.fs.Store
import net.runelite.cache.fs.jagex.DiskStorage
import java.io.File

class GameCache {

    lateinit var store: Store private set

    val disk: DiskStorage get() = store.storage as DiskStorage
    val archiveCount get() = store.indexes.size

    lateinit var configs: ConfigArchive private set
    lateinit var maps: MapArchive private set

    fun load(directory: File) {
        store = Store(directory)
        store.load()

        configs = ConfigArchive.load(this)
        maps = MapArchive.load(this)
    }

}