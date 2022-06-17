package dev.narcos.mapgen.engine.model.world

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

@kotlinx.serialization.Serializable
data class WorldPoint(
    val x: Int,
    val y: Int,
    val plane: Int,
) {
    constructor(
        i: Int,
        j: Int,
        x: Int,
        y: Int,
        plane: Int,
    ) : this(
        i shl 6 or x,
        j shl 6 or y,
        plane,
    )

    val region
        get(): Int {
            return x shr 6 shl 8 or (y shr 6)
        }

    fun derive(x: Int, y: Int, plane: Int): WorldPoint {
        return this.copy(
            x = this.x + x,
            y = this.y + y,
            plane = this.plane + plane
        )
    }
}

@kotlinx.serialization.Serializable
enum class Skill {
    ATTACK,
    DEFENCE,
    STRENGTH,
    HITPOINTS,
    RANGED,
    PRAYER,
    MAGIC,
    COOKING,
    WOODCUTTING,
    FLETCHING,
    FISHING,
    FIREMAKING,
    CRAFTING,
    SMITHING,
    MINING,
    HERBLORE,
    AGILITY,
    THIEVING,
    SLAYER,
    FARMING,
    RUNECRAFT,
    HUNTER,
    CONSTRUCTION,
}

@kotlinx.serialization.Serializable
enum class TransportRequirementType {
    SKILL, QUEST, ITEM, VARBIT
}

@kotlinx.serialization.Serializable
data class SkillRequirement(
    var type: TransportRequirementType,
    var level: Int? = null,
    var skill: Skill? = null,
    var quest: String? = null,
    var questState: List<String>? = null,
    var itemId: Int? = null,
    var quantity: Int? = null,
    var varbit: Int? = null,
    var varbitValue: Int? = null,
)

@kotlinx.serialization.Serializable
data class Transport(
    val source: WorldPoint,
    val destination: WorldPoint,
    val action: String,
    val objectName: String,
    val objectId: Int,
    val description: String? = null,
    val requirement: Array<SkillRequirement>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transport

        if (source != other.source) return false
        if (destination != other.destination) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + destination.hashCode()
        return result
    }
}

fun loadTransport(): List<Transport> {
    return Json.decodeFromString(Files.readString(Paths.get("src/main/resources/transports.json")))
}