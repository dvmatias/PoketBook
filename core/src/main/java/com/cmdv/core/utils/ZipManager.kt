package com.cmdv.core.utils

import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

class ZipManager {

    @Throws(IOException::class)
    fun unzip(zipFile: File?, targetDirectory: File?) {
        val zipInputStream = ZipInputStream(
            BufferedInputStream(FileInputStream(zipFile))
        )
        zipInputStream.use { zis ->
            var ze: ZipEntry
            var count: Int
            val buffer = ByteArray(8192)
            while (zis.nextEntry.also { ze = it } != null) {
                val file = File(targetDirectory, ze.name)
                val dir = if (ze.isDirectory) file else file.parentFile
                if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException(
                    "Failed to ensure directory: " +
                            dir.absolutePath
                )
                if (ze.isDirectory) continue
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.use {
                    while (zis.read(buffer).also { count = it } != -1) fileOutputStream.write(buffer, 0, count)
                }
                /* if time should be restored as well
                long time = ze.getTime();
                if (time > 0)
                    file.setLastModified(time);
                */
            }
        }
    }


    private var zipFile: ZipFile? = null

    fun fetchFromZip(fileName: String?): InputStream?  {
        var inputStream: InputStream? = null
        try {
            val zipFile = ZipFile(fileName)
            val containerEntry: ZipEntry? = zipFile.getEntry(fileName)
            containerEntry?.let {
                try {
                    inputStream = zipFile.getInputStream(it)
                } catch (e: IOException) {
                    Log.e("asdasda", "Error reading zip file $fileName", e)
                }
            }
        } catch (e: IOException) {
            Log.e("asdasadasd", "Error opening file", e)
        }

        return inputStream
    }

    @Throws(IOException::class)
    fun unzipA(zipFile: File?, targetDirectory: File?) {
        val zis = ZipInputStream(
            BufferedInputStream(FileInputStream(zipFile))
        )
        try {
            var ze: ZipEntry
            var count: Int
            val buffer = ByteArray(8192)
            while (zis.nextEntry.also { ze = it } != null) {
                val file = File(targetDirectory, ze.name)
                val dir = if (ze.isDirectory) file else file.parentFile
                if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException(
                    "Failed to ensure directory: " +
                            dir.absolutePath
                )
                if (ze.isDirectory) continue
                val fout = FileOutputStream(file)
                try {
                    while (zis.read(buffer).also { count = it } != -1) fout.write(buffer, 0, count)
                } finally {
                    fout.close()
                }
                /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close()
        }
    }

}