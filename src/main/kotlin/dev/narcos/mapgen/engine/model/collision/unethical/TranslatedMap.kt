package dev.narcos.mapgen.engine.model.collision.unethical

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.GZIPOutputStream

class TranslatedMap(cm: GlobalUnethicalCollisionMap) {

    val regions: Array<BitSet4D?> = arrayOfNulls(256 * 256)

    init {
        cm.regions.indices.forEach {
            if (cm.regions[it].bits.cardinality() != 0) {
                regions[it] = cm.regions[it]
            }
        }
    }

    fun writeToFile(): File {
        val bytes = toBytes()
        val fileLoc = File("data/regions")
        if (!fileLoc.isFile) {
            try {
                fileLoc.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            ByteArrayOutputStream(bytes.size).use { bos ->
                GZIPOutputStream(bos).use { gos ->
                    FileOutputStream(fileLoc).use { fos ->
                        gos.write(bytes)
                        gos.finish()
                        fos.write(bos.toByteArray())
                        gos.flush()
                        fos.flush()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fileLoc
    }

    private fun toBytes(): ByteArray {
        val regionCount = regions.filterNotNull().count()
        val buffer = ByteBuffer.allocate(regionCount * (2 + 64 * 64 * 4 * 2 / 8))
        for (i in regions.indices) {
            val region = regions[i]
            if (region != null) {
                buffer.putShort(i.toShort())
                region.write(buffer)
            }
        }
        return buffer.array()
    }

    fun gzipped(): ByteArray {
        val bytes = ByteArrayOutputStream().use { os ->
            GZIPOutputStream(os).use { gos ->
                gos.write(toBytes())
                gos.finish()
                os.toByteArray()
            }
        }
        return bytes
    }

}