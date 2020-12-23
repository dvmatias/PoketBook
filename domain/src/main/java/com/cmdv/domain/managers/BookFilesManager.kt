package com.cmdv.domain.managers

import java.io.File

interface BookFilesManager {

    fun getPdfAndEpubFiles(): List<File>

    fun isEpubFile(fileName: String): Boolean

    fun isPdfFile(fileName: String): Boolean

}