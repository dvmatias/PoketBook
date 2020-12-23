package com.cmdv.data.repositories

import com.cmdv.domain.managers.FilesManager
import com.cmdv.domain.models.EpubModel
import com.cmdv.domain.models.PdfModel
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FilesRepositoryImpl(
    private val filesManager: FilesManager
) : FilesRepository {

    override fun getEpubFiles(): Flow<List<EpubModel>?> =
        flow {
            emit(filesManager.getEpubFilesManager())
        }.flowOn(Dispatchers.Default)

    override fun getPdfFiles(): Flow<List<PdfModel>?> =
        flow {
            emit(filesManager.getPdfFilesManager())
        }.flowOn(Dispatchers.Default)

}