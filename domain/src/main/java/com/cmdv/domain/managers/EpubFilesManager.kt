package com.cmdv.domain.managers

import com.cmdv.domain.models.epub.EpubModel

interface EpubFilesManager {

    fun getEpubFiles(): List<EpubModel>

}