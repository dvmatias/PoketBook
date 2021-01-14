package com.cmdv.domain.models

open class DocumentModel {
    open val fileName: String = ""
    open val filePath: String = ""
    open val fileType: DocumentType = DocumentType.DEFAULT
    open val fileSize: Long = 0
}

enum class DocumentType {
    DEFAULT,
    EPUB,
    PDF
}