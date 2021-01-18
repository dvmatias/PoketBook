package com.cmdv.domain.providers

import com.cmdv.domain.models.epub.EpubModel

interface EpubFilesProvider {

    fun getEpubFiles(): List<EpubModel>

}