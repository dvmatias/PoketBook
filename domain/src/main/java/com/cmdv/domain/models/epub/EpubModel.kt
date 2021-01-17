package com.cmdv.domain.models.epub

import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.FileModel

data class EpubModel(
    override val fileType: DocumentType,
    val file: FileModel,
    val title: String,
    val author: String,
    val language: String,
    val series: String,
    val seriesIndex: String,
    val format: String,
) : DocumentModel()