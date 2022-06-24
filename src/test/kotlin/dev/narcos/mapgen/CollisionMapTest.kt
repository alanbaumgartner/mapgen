package dev.narcos.mapgen

import dev.narcos.mapgen.engine.MapGenerator
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertContentEquals

class CollisionMapTest {

    @Test
    fun `that new collision map is correct`() {
        val mapGenerator = MapGenerator()
        mapGenerator.start("src/test/resources/cache/")
        val newBytes = mapGenerator.dumpCollisionMap()
        val oldBytes = Files.readAllBytes(Paths.get("src/test/resources/regions"))
        assertContentEquals(oldBytes, newBytes)
    }

}