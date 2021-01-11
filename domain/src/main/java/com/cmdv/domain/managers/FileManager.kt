package com.cmdv.domain.managers

import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.PdfModel

interface FileManager : EpubFilesManager, PdfFilesManager {

    override fun getEpubFiles(): List<EpubModel>
    override fun getPdfFiles(): List<PdfModel>

}