package com.cmdv.domain.managers

import com.cmdv.domain.models.EpubModel
import com.cmdv.domain.models.PdfModel

interface FilesManager : EpubFilesManager, PdfFilesManager {

    override fun getEpubFilesManager(): List<EpubModel>?
    override fun getPdfFilesManager(): List<PdfModel>?

}