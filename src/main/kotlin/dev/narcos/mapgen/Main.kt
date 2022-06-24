package dev.narcos.mapgen

import dev.narcos.mapgen.engine.MapGenerator
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val mapGenerator = MapGenerator()
    mapGenerator.start("data/cache/")
    Files.write(Paths.get("data/regions"), mapGenerator.dumpCollisionMap())
}