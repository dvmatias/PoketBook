package com.cmdv.domain.providers

import com.cmdv.domain.models.pdf.PdfModel

interface PdfFilesProvider {

    fun getPdfFiles(): List<PdfModel>

}