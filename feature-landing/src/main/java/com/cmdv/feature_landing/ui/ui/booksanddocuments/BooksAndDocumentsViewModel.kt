package com.cmdv.feature_landing.ui.ui.booksanddocuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cmdv.core.managers.FilesManagerImpl
import com.cmdv.data.repositories.FilesRepositoryImpl
import com.cmdv.domain.repositories.FilesRepository
import java.io.File

class BooksAndDocumentsViewModel : ViewModel() {

    private var filesRepository: FilesRepository = FilesRepositoryImpl(FilesManagerImpl())

    private val _allFiles = filesRepository.getAllFiles().asLiveData(viewModelScope.coroutineContext)
    val allFiles: LiveData<List<File>>
        get() = _allFiles


}