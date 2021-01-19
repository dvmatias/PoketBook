package com.cmdv.domain.models

abstract class DocumentModel(
    val id: String,
    val fileType: String,
)

enum class DocumentType(val format: String) {
    DEFAULT(""),
    EPUB("epub"),
    PDF("pdf")
}