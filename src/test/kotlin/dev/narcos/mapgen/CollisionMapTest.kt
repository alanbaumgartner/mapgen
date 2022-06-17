package dev.narcos.mapgen

import dev.narcos.mapgen.engine.Engine
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertContentEquals

class CollisionMapTest {

    @Test
    fun `that collision map is correct`() {
        val engine = Engine()
        engine.start("src/test/resources/cache/")
        val newBytes = engine.dumpCollisionMap()
        val oldBytes = Files.readAllBytes(Paths.get("src/test/resources/regions-old"))
        assertContentEquals(oldBytes, newBytes)
    }

}