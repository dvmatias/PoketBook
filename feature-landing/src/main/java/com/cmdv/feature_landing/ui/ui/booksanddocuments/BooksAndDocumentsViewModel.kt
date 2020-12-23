package com.cmdv.feature_landing.ui.ui.booksanddocuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cmdv.core.managers.EpubBookManagerImpl
import com.cmdv.core.managers.BookFilesManagerImpl
import com.cmdv.core.managers.ZipManager
import com.cmdv.data.repositories.FilesRepositoryImpl
import com.cmdv.domain.managers.EpubBookManager
import com.cmdv.domain.repositories.FilesRepository
import java.io.File

class BooksAndDocumentsViewModel : ViewModel() {

    private var filesRepository: FilesRepository = FilesRepositoryImpl(BookFilesManagerImpl())
    private var epubBookManager: EpubBookManager = EpubBookManagerImpl(ZipManager())

    private val _allFiles = filesRepository.getAllFiles().asLiveData(viewModelScope.coroutineContext)
    val allFiles: LiveData<List<File>>
        get() = _allFiles

    fun fetchEpubBooks(allFiles: List<File>) {
        val epubBooks = epubBookManager.getEpubBooks()
    }

}