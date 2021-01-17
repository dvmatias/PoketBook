package com.cmdv.data.entity

import com.cmdv.domain.models.DocumentType

data class FileEntity (
    val name: String,
    val path: String,
    val type: DocumentType,
    val size: Long
)
