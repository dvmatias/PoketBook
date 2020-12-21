package com.cmdv.domain.managers

import java.io.File

interface FilesManager {

    fun getPdfAndEpubFiles(): List<File>

}