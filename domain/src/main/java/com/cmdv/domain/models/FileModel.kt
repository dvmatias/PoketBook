package com.cmdv.domain.models

data class FileModel(
    val name: String,
    val path: String,
    val type: DocumentType,
    val size: Long
)
