package com.cmdv.core.managers

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ZipManager {

    fun getEpubZipFileInputStream(filePath: String?): InputStream? {
//        var zipInputStream: ZipInputStream? = null
//        filePath?.run {
//            try {
//                zipInputStream = ZipInputStream(File(this).inputStream())
//            } catch (e: IOException) {
//                Log.e("", "Error opening $filePath", e)
//            }
//        } ?: Log.e("", "Error opening file. File path is null.")
//        return zipInputStream
        var inputS: InputStream? = null
        val zipFile = ZipFile("/storage/emulated/0/Download/El oro del depredador - Philip Reeve.epub")
        val containerEntry: ZipEntry = zipFile.getEntry("META-INF/container.xml")
        if (containerEntry != null) {
            try {
                inputS = zipFile.getInputStream(containerEntry)
            } catch (e: IOException) {
                Log.e("asdad", "Error reading zip file $filePath", e)
            }
        }
        return inputS

    }
}