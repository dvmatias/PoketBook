package com.cmdv.domain.providers

import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.pdf.PdfModel

interface FilesProvider : EpubFilesProvider, PdfFilesProvider {

    override fun getEpubFiles(): List<EpubModel>
    override fun getPdfFiles(): List<PdfModel>

}