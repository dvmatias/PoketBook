package com.cmdv.feature_landing.ui.ui.booksanddocuments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.data.providers.FilesProviderImpl
import com.cmdv.data.repositories.FileRepositoryImpl
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BooksAndDocumentsViewModel(
    private val filesRepository: FilesRepository
) : ViewModel() {

    private val mutableDocumentsLiveData = MutableLiveData<List<DocumentModel>>()
    private var getDocumentsJob: Job? = null
    val documentsLiveData = mutableDocumentsLiveData

    fun getDocuments() {
        getDocumentsJob.cancelIfActive()
        getDocumentsJob = viewModelScope.launch {
            filesRepository.getDocuments().collect { documents ->
                mutableDocumentsLiveData.value = documents?.distinctBy { it.id }
            }
        }
    }

}

private fun Job?.cancelIfActive() {
    this?.let { if (isActive) cancel() }
}
