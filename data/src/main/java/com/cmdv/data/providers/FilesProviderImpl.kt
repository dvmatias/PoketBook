package com.cmdv.data.providers

import android.os.Environment
import com.cmdv.data.entity.epub.EpubEntity
import com.cmdv.data.mappers.EpubMapper
import com.cmdv.data.parsers.FileParserFactory
import com.cmdv.data.parsers.FileType
import com.cmdv.domain.providers.FilesProvider
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.pdf.PdfModel
import java.util.*
import java.util.regex.Pattern

private const val FILE_EXTENSION_EPUB = ".*epub$"
private const val FILE_EXTENSION_PDF = ".*pdf$"

class FilesProviderImpl : FilesProvider {

    private var fileNames: List<String> = listOf()
    private val fileParserFactory: FileParserFactory = FileParserFactory()

    init {
        fileNames = getFileNames()
    }

    override fun getEpubFiles(): List<EpubModel> {
        val epubs = arrayListOf<EpubModel>()
        val parser = fileParserFactory.newFileParser(FileType.EPUB)
        fileNames.filter { isEpubFile(it) }.forEach { fileName ->
            val epub: EpubEntity? = parser.parse(fileName)
            epub?.run { epubs.add(EpubMapper().transformEntityToModel(epub)) }
        }
        return epubs
    }

    override fun getPdfFiles(): List<PdfModel> {
        val pdfs = arrayListOf<PdfModel>()
        val parser = fileParserFactory.newFileParser(FileType.PDF)
        fileNames.filter { isPdfFile(it) }.forEach { fileName ->
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