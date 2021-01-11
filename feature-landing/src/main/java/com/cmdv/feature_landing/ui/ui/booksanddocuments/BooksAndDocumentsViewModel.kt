package com.cmdv.feature_landing.ui.ui.booksanddocuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cmdv.core.managers.FileManagerImpl
import com.cmdv.data.repositories.FileRepositoryImpl
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.repositories.FilesRepository
import java.io.File

class BooksAndDocumentsViewModel : ViewModel() {

    private var filesRepository: FilesRepository = FileRepositoryImpl(FileManagerImpl())

    private val _allFiles = filesRepository.fetchEpubFiles().asLiveData(viewModelScope.coroutineContext)
    val allFiles: LiveData<List<EpubModel>?>
        get() = _allFiles

}