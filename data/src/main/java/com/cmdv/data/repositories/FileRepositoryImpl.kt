package com.cmdv.data.repositories

import com.cmdv.domain.managers.FileManager
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.PdfModel
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FileRepositoryImpl(
    private val fileManager: FileManager
) : FilesRepository {

    override fun getDocuments() = flow {
        var caca: List<DocumentModel>?
        withContext(Dispatchers.IO) {
            val a = async { fetchEpubs() }
            val b = async { fetchPdfs() }
            caca = a.await() + b.await()
        }
        emit(caca)
    }

    private suspend fun fetchEpubs(): List<EpubModel> =
        withContext(Dispatchers.IO) {
            fileManager.getEpubFiles()
        }

    private suspend fun fetchPdfs(): List<PdfModel> =
        withContext(Dispatchers.IO) {
            fileManager.getPdfFiles()
        }
}