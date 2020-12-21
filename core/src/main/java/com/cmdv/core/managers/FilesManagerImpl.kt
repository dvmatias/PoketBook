package com.cmdv.core.managers

import android.os.Environment
import com.cmdv.domain.managers.FilesManager
import java.io.File

class FilesManagerImpl : FilesManager {

    private val pdfFiles = arrayListOf<File>()
    private val epubFiles = arrayListOf<File>()

    override fun getPdfAndEpubFiles(): List<File> {
        setFiles(Environment.getExternalStorageDirectory())
        val list = arrayListOf<File>()
        list.addAll(epubFiles)
        list.addAll(pdfFiles)
        return list
    }

    private fun setFiles(dir: File) {
        val pdfPattern = ".pdf"
        val epubPattern = ".epub"
        val listFile = dir.listFiles()

        listFile?.forEach { file ->
            if (file.isDirectory) {
                setFiles(file)
            } else {
                when {
                    file.name.endsWith(pdfPattern) -> pdfFiles.add(file)
                    file.name.endsWith(epubPattern) -> epubFiles.add(file)
                }
            }
        }
    }
}