package com.cmdv.domain.repositories

import com.cmdv.domain.models.DocumentModel
import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    fun getDocuments(): Flow<List<DocumentModel>?>

}