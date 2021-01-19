package com.cmdv.domain.models

open class DocumentModel {
    open val id: String = ""
    open val fileType: DocumentType = DocumentType.DEFAULT
}

enum class DocumentType(val format: String) {
    DEFAULT(""),
    EPUB("epub"),
    PDF("pdf")
}