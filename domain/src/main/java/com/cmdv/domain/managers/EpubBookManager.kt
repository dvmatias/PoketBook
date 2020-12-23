package com.cmdv.domain.managers

import com.cmdv.domain.models.BookModel

interface EpubBookManager {

    fun getEpubBooks(): List<BookModel>?

    fun getEpubBook(filePath: String?): BookModel?

}