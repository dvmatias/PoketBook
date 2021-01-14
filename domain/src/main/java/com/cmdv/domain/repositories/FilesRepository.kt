package com.cmdv.domain.repositories

import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.PdfModel
import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    fun getDocuments(): Flow<List<DocumentModel>?>

}