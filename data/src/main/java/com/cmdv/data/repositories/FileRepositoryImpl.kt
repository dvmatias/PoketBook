package com.cmdv.data.repositories

import com.cmdv.domain.providers.FilesProvider
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.pdf.PdfModel
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FileRepositoryImpl(
    private val filesProvider: FilesProvider
) : FilesRepository {

    override fun getDocuments() = flow {
        var documents: List<DocumentModel>?
        withContext(Dispatchers.IO) {
            val epubsDeferredJob: Deferred<List<EpubModel>> = async { fetchEpubs() }
            val pdfsDeferredJob: Deferred<List<PdfModel>> = async { fetchPdfs() }

            documents = epubsDeferredJob.await() + pdfsDeferredJob.await()
        }
        emit(documents)
    }

    private suspend fun fetchEpubs(): List<EpubModel> =
        withContext(Dispatchers.IO) {
            filesProvider.getEpubFiles()
        }

    private suspend fun fetchPdfs(): List<PdfModel> =
        withContext(Dispatchers.IO) {
            filesProvider.getPdfFiles()
        }
}