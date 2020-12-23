package com.cmdv.domain.managers

import com.cmdv.domain.models.PdfModel

interface PdfFilesManager {

    fun getPdfFilesManager(): List<PdfModel>?

}