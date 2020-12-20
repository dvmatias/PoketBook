package com.cmdv.data.repositories

import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FilesRepositoryImpl : FilesRepository {

    override fun getAllFiles(): Flow<List<String>> {
        return flow {
            val list = listOf<String>("a", "b", "c")
            emit(list)
        }.flowOn(Dispatchers.Default)
    }

}