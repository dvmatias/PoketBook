package com.cmdv.core.managers

import android.os.Environment
import com.cmdv.domain.managers.FilesManager
import com.cmdv.domain.models.EpubModel
import com.cmdv.domain.models.PdfModel
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

private const val FILE_EXTENSION_EPUB = ".*epub$"
private const val FILE_EXTENSION_PDF = ".*pdf$"

class FilesManagerImpl : FilesManager {

    private var epubBookManager: EpubBookManager = EpubBookManager(ZipManager())
    private var fileNames: List<String> = listOf()

    init {
        fileNames = getFileNames()
    }

    override fun getEpubFilesManager(): List<EpubModel> =
        epubBookManager.getEpubBooks(fileNames.filter { isEpubFile(it) })

    override fun getPdfFilesManager(): List<PdfModel>? {
        // TODO
        return null
    }

    private fun getFileNames(): List<String> {
        val fileNames = arrayListOf<String>()
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filesInDirectory = path.listFiles()
        filesInDirectory?.forEach { file ->
            fileNames.add(file.name)
        }
        return fileNames
    }

    private fun isEpubFile(fileName: String): Boolean =
        Pattern.compile(FILE_EXTENSION_EPUB).matcher(fileName.toLowerCase(Locale.getDefault())).matches()

    private fun isPdfFile(fileName: String): Boolean =
        Pattern.compile(FILE_EXTENSION_PDF).matcher(fileName.toLowerCase(Locale.getDefault())).matches()
}