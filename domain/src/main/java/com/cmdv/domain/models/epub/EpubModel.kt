package com.cmdv.domain.models.epub

import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.FileModel

data class EpubModel(
    override val fileType: DocumentType,
    val file: FileModel,
    val title: String,
    val author: String,
    val cover: CoverModel,
    val language: String,
    val series: String,
    val seriesIndex: String,
    val format: String,
) : DocumentModel() {

    data class CoverModel(
        val image: String,
        val mediaType: String
    )

}