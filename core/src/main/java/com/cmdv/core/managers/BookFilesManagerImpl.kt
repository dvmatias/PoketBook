package com.cmdv.core.managers

import android.os.Environment
import com.cmdv.domain.managers.BookFilesManager
import java.io.File
import java.util.*
import java.util.regex.Pattern

private const val FILE_EXTENSION_EPUB = ".*epub$"
private const val FILE_EXTENSION_PDF = ".*pdf$"

class BookFilesManagerImpl : BookFilesManager {

    private val pdfFiles = arrayListOf<File>()
    private val epubFiles = arrayListOf<File>()

    override fun getPdfAndEpubFiles(): List<File> {
        setFiles()
        val list = arrayListOf<File>()
        list.addAll(epubFiles)
        list.addAll(pdfFiles)
        return list
    }

    override fun isEpubFile(fileName: String): Boolean =
        Pattern.compile(FILE_EXTENSION_EPUB).matcher(fileName.toLowerCase(Locale.getDefault())).matches()

    override fun isPdfFile(fileName: String): Boolean =
        Pattern.compile(FILE_EXTENSION_PDF).matcher(fileName.toLowerCase(Locale.getDefault())).matches()

    private fun setFiles() {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filesInDirectory = path.listFiles()
        filesInDirectory?.forEach { file ->
            val fileName = file.name
            when {
                isEpubFile(fileName) -> epubFiles.add(file)
                isPdfFile(fileName) -> pdfFiles.add(file)
            }
        }
        epubFiles.sort()
        pdfFiles.sort()
    }
}