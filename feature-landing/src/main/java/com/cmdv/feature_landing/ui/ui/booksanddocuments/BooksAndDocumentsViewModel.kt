package com.cmdv.feature_landing.ui.ui.booksanddocuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cmdv.core.managers.FilesManagerImpl
import com.cmdv.data.repositories.FilesRepositoryImpl
import com.cmdv.domain.models.EpubModel
import com.cmdv.domain.repositories.FilesRepository
import java.io.File

class BooksAndDocumentsViewModel : ViewModel() {

    private var filesRepository: FilesRepository = FilesRepositoryImpl(FilesManagerImpl())

    private val _allFiles = filesRepository.getEpubFiles().asLiveData(viewModelScope.coroutineContext)
    val allFiles: LiveData<List<EpubModel>?>
        get() = _allFiles

    fun fetchEpubBooks(allFiles: List<File>) {
        val epubBooks =  filesRepository.getEpubFiles().asLiveData(viewModelScope.coroutineContext)
    }

}