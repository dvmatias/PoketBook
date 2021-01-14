package com.cmdv.feature_landing.ui.ui.booksanddocuments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.core.managers.FileManagerImpl
import com.cmdv.data.repositories.FileRepositoryImpl
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.repositories.FilesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BooksAndDocumentsViewModel : ViewModel() {

    private var filesRepository: FilesRepository = FileRepositoryImpl(FileManagerImpl())

    private val mutableDocumentsLiveData = MutableLiveData<List<DocumentModel>>()
    private var getDocumentsJob: Job? = null
    val documentsLiveData = mutableDocumentsLiveData

    fun getDocuments() {
        getDocumentsJob.cancelIfActive()
        getDocumentsJob = viewModelScope.launch {
            filesRepository.getDocuments().collect {
                mutableDocumentsLiveData.value = it
            }
        }
    }

}

private fun Job?.cancelIfActive() {
    this?.let { if (isActive) cancel() }
}
