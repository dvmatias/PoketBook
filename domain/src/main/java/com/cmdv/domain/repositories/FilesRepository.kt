package com.cmdv.domain.repositories

import kotlinx.coroutines.flow.Flow
import java.io.File

interface FilesRepository {

    fun getAllFiles(): Flow<List<File>>

}