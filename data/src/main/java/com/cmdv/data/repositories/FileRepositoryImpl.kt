package com.cmdv.data.repositories

import com.cmdv.domain.managers.FileManager
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.PdfModel
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FileRepositoryImpl(
    private val fileManager: FileManager
) : FilesRepository {

    override fun fetchEpubFiles(): Flow<List<EpubModel>?> =
        flow {
            emit(fileManager.getEpubFiles())
        }.flowOn(Dispatchers.Default)

    override fun fetchPdfFiles(): Flow<List<PdfModel>?> =
        flow {
            emit(fileManager.getPdfFiles())
        }.flowOn(Dispatchers.Default)

}