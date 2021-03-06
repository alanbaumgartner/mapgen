package dev.narcos.mapgen.config

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.toValue
import java.io.File
import java.io.FileNotFoundException

class XteaConfig {

    private var loaded = false
    private var config = Config()
    private val file = File("data/cache/xteas.json")

    private val xteaKeys = mutableMapOf<Int, IntArray>()

    fun load() {
        if (loaded) return
        loaded = true

        if (!file.exists()) {
            throw FileNotFoundException("Could not load region XTEA keys config file from: data/cache/xteas.json.")
        }

        val entries = config.from.json.file(file).toValue<Array<Xtea>>().map { it.toXteaEntry() }
        entries.forEach { xteaKeys[it.region] = it.keys }
    }

    operator fun get(regionId: Int): IntArray = xteaKeys[regionId] ?: IntArray(4) { 0 }

    data class Xtea(
        val archive: Int,
        val group: Int,
        val name_hash: Int,
        val name: String,
        val mapsquare: Int,
        val key: IntArray,
    ) {
        fun toXteaEntry(): XteaEntry {
            return XteaEntry(mapsquare, key)
        }
    }

    data class XteaEntry(val region: Int, val keys: IntArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as XteaEntry

            if (region != other.region) return false
            if (!keys.contentEquals(other.keys)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = region
            result = 31 * result + keys.contentHashCode()
            return result
        }
    }

    companion object {
        private val config: XteaConfig = XteaConfig()

        fun load() = config.load()

        val regions get() = config.xteaKeys

        fun getRegionKey(regionId: Int) = config[regionId]
    }
}