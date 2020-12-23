package com.cmdv.domain.repositories

import com.cmdv.domain.models.EpubModel
import com.cmdv.domain.models.PdfModel
import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    fun getEpubFiles(): Flow<List<EpubModel>?>
    fun getPdfFiles(): Flow<List<PdfModel>?>

}