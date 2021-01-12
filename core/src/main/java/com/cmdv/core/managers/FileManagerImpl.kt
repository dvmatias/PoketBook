package com.cmdv.core.managers

import android.os.Environment
import com.cmdv.data.parsers.FileParser
import com.cmdv.data.parsers.FileParserFactory
import com.cmdv.data.parsers.FileType
import com.cmdv.domain.managers.FileManager
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.PdfModel
import java.util.*
import java.util.regex.Pattern

private const val FILE_EXTENSION_EPUB = ".*epub$"
private const val FILE_EXTENSION_PDF = ".*pdf$"

class FileManagerImpl : FileManager {

    private var fileNames: List<String> = listOf()
    private val fileParserFactory: FileParserFactory = FileParserFactory()
    private lateinit var parser: FileParser

    init {
        fileNames = getFileNames()
    }

    override fun getEpubFiles(): List<EpubModel> {
        val epubs = arrayListOf<EpubModel>()
        parser = fileParserFactory.newFileParser(FileType.EPUB)
        fileNames.filter { isEpubFile(it) }.forEach { fileName ->
            val epub: EpubModel? = parser.parse(fileName)
            epub?.run { epubs.add(this) }
        }
        return epubs
    }

    override fun getPdfFiles(): List<PdfModel> {
        val pdfs = arrayListOf<PdfModel>()
        parser = fileParserFactory.newFileParser(FileType.PDF)
        fileNames.filter { isEpubFile(it) }.forEach { fileName ->
            val pdf: PdfModel? = parser.parse(fileName)
            pdf?.run { pdfs.add(this) }
        }
        return pdfs
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