package dev.narcos.mapgen.cache.config

import dev.narcos.mapgen.cache.GameCache
import dev.narcos.mapgen.common.inject
import net.runelite.cache.ObjectManager
import net.runelite.cache.definitions.ObjectDefinition

class ObjectConfig private constructor(
    entries: MutableMap<Int, ObjectDefinition> = mutableMapOf(),
) : Map<Int, ObjectDefinition> by entries {
    companion object {
        private val cache: GameCache by inject()

        fun load(): ObjectConfig {
            val entries = mutableMapOf<Int, ObjectDefinition>()

            val manager = ObjectManager(cache.store)
            manager.load()
            manager.objects.forEach { objDef ->
                entries[objDef.id] = objDef
            }

            return ObjectConfig(entries)
        }
    }
}