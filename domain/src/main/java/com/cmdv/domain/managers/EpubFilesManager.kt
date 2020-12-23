package com.cmdv.domain.managers

import com.cmdv.domain.models.EpubModel

interface EpubFilesManager {

    fun getEpubFilesManager(): List<EpubModel>?

}