package com.cmdv.domain.repositories

import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.PdfModel
import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    fun fetchEpubFiles(): Flow<List<EpubModel>?>
    fun fetchPdfFiles(): Flow<List<PdfModel>?>

}