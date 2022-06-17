package dev.narcos.mapgen.engine.model.world

import dev.narcos.mapgen.cache.GameCache
import dev.narcos.mapgen.common.inject
import dev.narcos.mapgen.config.XteaConfig
import dev.narcos.mapgen.engine.model.collision.CollisionFlag
import dev.narcos.mapgen.engine.model.collision.CollisionMap
import dev.narcos.mapgen.engine.model.map.Tile
import dev.narcos.mapgen.engine.model.obj.GameObject
import net.runelite.cache.definitions.LocationsDefinition
import net.runelite.cache.definitions.MapDefinition
import net.runelite.cache.region.Region
import java.io.FileNotFoundException

class World {

    var count = 0
    private val cache: GameCache by inject()

    val collision = CollisionMap()

    internal fun load() {
        /*
         * Load World map collision and objects.
         */

        XteaConfig.regions.forEach { (regionId, keys) ->
            val (map, loc) = cache.maps[regionId] ?: return@forEach
            Region(regionId).loadCollision(map, loc)
        }

        val cm = GlobalCollisionMap()

        XteaConfig.regions.forEach { (regionId, keys) ->
            for (plane in 0 until MAX_PLANE) {
                for (x in 0 until 64) {
                    for (y in 0 until 64) {
                        val tile = dev.narcos.mapgen.engine.model.map.Region(regionId).toTile(plane).translate(x, y)
                        val flag = collision[tile.x, tile.y, tile.plane]

                        val north = collision[tile.x, tile.y + 1, tile.plane]
                        val east = collision[tile.x + 1, tile.y, tile.plane]

                        if (isObstacle(flag)) {
                            cm.set(tile.x, tile.y, tile.plane, 0, false)
                            cm.set(tile.x, tile.y, tile.plane, 1, false)
                        } else {
                            cm.set(tile.x, tile.y, tile.plane, 0, true)
                            cm.set(tile.x, tile.y, tile.plane, 1, true)

                            if (isWalled(flag, Direction.NORTH) || isObstacle(north)) {
                                cm.set(tile.x, tile.y, tile.plane, 0, false)
                            }

                            if (isWalled(flag, Direction.EAST) || isObstacle(east)) {
                                cm.set(tile.x, tile.y, tile.plane, 1, false)
                            }
                        }

                    }
                }
            }
        }
        val tm = TranslatedMap(cm)
        tm.writeToFile()
    }

    enum class Direction(val flag: Int) {
        NORTH(0x2),
        WEST(0x80),
        SOUTH(0x20),
        EAST(0x8)
    }


    fun isObstacle(flag: Int): Boolean {
        if (flag == 0) {
            return false
        }

        return check(flag, 0x100 or 0x20000 or 0x200000 or 0x1000000)
    }

    fun check(flag: Int, checkFlag: Int): Boolean {
        return (flag and checkFlag != 0)
    }

    fun isWalled(flag: Int, direction: Direction): Boolean {
//        if (flag == 0) {
//            return false
//        }

        return check(flag, direction.flag)
    }


    private fun Region.loadCollision(map: MapDefinition, loc: LocationsDefinition) {
        val floorMask = mutableMapOf<Tile, Int>()

        loadTerrain(map)
        loadLocations(loc)

        for (plane in 0 until MAX_PLANE) {
            for (x in 0 until 64) {
                for (y in 0 until 64) {
                    floorMask[Tile(x, y, plane)] = getTileSetting(plane, x, y).toInt()
                }
            }
        }

        for (plane in 0 until MAX_PLANE) {
            for (x in 0 until 64) {
                for (y in 0 until 64) {
                    val localTile = Tile(x, y, plane)
                    val localMask = floorMask[localTile] ?: 0
                    if ((localMask and BLOCKED_TILE_FLAG) != BLOCKED_TILE_FLAG) {
                        continue
                    }
                    var adjustedPlane = plane
                    val bridgeTile = Tile(x, y, 1)
                    val bridgeMask = floorMask[bridgeTile] ?: 0
                    if ((bridgeMask and BRIDGE_TILE_FLAG) == BRIDGE_TILE_FLAG) {
                        adjustedPlane--
                    }
                    if (adjustedPlane >= 0) {
                        val tile =
                            dev.narcos.mapgen.engine.model.map.Region(this.regionID).toTile(plane).translate(x, y)
                        collision.add(tile, CollisionFlag.FLOOR)
                    }
                }
            }
        }

        loc.locations.forEach {
            val localX = it.position.x
            val localY = it.position.y
            if (localX !in 0 until 64 || localY !in 0 until 64) {
                return@forEach
            }
            val shape = it.type
            val rotation = it.orientation
            var plane = it.position.z
            val localTile = Tile(localX, localY, 1)
            val floor = floorMask[localTile] ?: 0
            if ((floor and BRIDGE_TILE_FLAG) == BRIDGE_TILE_FLAG) {
                plane--
            }
            if (plane >= 0) {
                val data = cache.configs.objects[it.id]
                    ?: throw FileNotFoundException("Failed to find cache object config for object ID: ${it.id}.")
                val tile =
                    dev.narcos.mapgen.engine.model.map.Region(this.regionID).toTile(plane).translate(localX, localY)
                val obj = GameObject(data, tile, shape, rotation)
                collision.addObject(obj)
            }
        }
    }

    companion object {
        private const val MAX_PLANE = 4
        private const val BLOCKED_TILE_FLAG = 0x1
        private const val BRIDGE_TILE_FLAG = 0x2
    }
}