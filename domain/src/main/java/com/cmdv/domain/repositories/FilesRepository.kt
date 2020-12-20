package com.cmdv.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    fun getAllFiles(): Flow<List<String>>

}