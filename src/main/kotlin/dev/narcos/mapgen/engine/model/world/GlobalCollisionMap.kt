package dev.narcos.mapgen.engine.model.world

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.GZIPOutputStream

class GlobalCollisionMap : CollisionMap2 {
    val regions: Array<BitSet4D> = Array(256 * 256) {
        BitSet4D(64, 64, 4, 2)
    }

    //    constructor(data: ByteArray) {
//        val buffer = ByteBuffer.wrap(data)
//        while (buffer.hasRemaining()) {
//            val region = buffer.short.toInt() and 0xffff
//            regions[region] = BitSet4D(buffer, 64, 64, 4, 2)
//        }
//    }

//    fun writeToFile(): File {
//        val bytes = toBytes()
//        val fileLoc = File("data/regions")
//        if (!fileLoc.isFile) {
//            try {
//                fileLoc.createNewFile()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        try {
//            ByteArrayOutputStream(bytes.size).use { bos ->
//                GZIPOutputStream(bos).use { gos ->
//                    FileOutputStream(fileLoc).use { fos ->
//                        gos.write(bytes)
//                        gos.finish()
//                        fos.write(bos.toByteArray())
//                        gos.flush()
//                        fos.flush()
//                    }
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return fileLoc
//    }

//    fun toBytes(): ByteArray {
//        val buffer = ByteBuffer.allocate(regions.size * (2 + 64 * 64 * 4 * 2 / 8))
//        for (i in regions.indices) {
//            buffer.putShort(i.toShort())
//            regions[i].write(buffer)
//        }
//        return buffer.array()
//    }

    operator fun set(x: Int, y: Int, z: Int, w: Int, value: Boolean) {
        val region = regions[x / 64 * 256 + y / 64]
        region[x % 64, y % 64, z, w] = value
    }

    fun getRegion(x: Int, y: Int): BitSet4D {
        val regionId = x / 64 * 256 + y / 64
        return regions[regionId]
    }

//    fun createRegion(region: Int) {
//        regions[region] = BitSet4D(64, 64, 4, 2)
//        regions[region].setAll(true)
//    }

    operator fun get(x: Int, y: Int, z: Int, w: Int): Boolean {
        val region = getRegion(x, y)
        val regionX = x % 64
        val regionY = y % 64
        return region[regionX, regionY, z, w]
    }

//    fun overwrite(globalCollisionMap: GlobalCollisionMap) {
//        System.arraycopy(globalCollisionMap.regions, 0, regions, 0, regions.size)
//    }

    override fun n(x: Int, y: Int, z: Int): Boolean {
        return get(x, y, z, 0)
    }

    override fun e(x: Int, y: Int, z: Int): Boolean {
        return get(x, y, z, 1)
    }
}