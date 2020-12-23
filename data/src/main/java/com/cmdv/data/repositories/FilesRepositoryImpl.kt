package com.cmdv.data.repositories

import com.cmdv.domain.managers.BookFilesManager
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class FilesRepositoryImpl(
    private val bookFilesManager: BookFilesManager
) : FilesRepository {

    override fun getAllFiles(): Flow<List<File>> {
        return flow {
            emit(bookFilesManager.getPdfAndEpubFiles())
        }.flowOn(Dispatchers.Default)
    }

}