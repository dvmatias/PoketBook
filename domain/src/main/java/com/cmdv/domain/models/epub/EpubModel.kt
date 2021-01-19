package com.cmdv.domain.models.epub

import android.graphics.Bitmap
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.FileModel

data class EpubModel(
    val uuid: String,
    val format: String,
    val file: FileModel,
    val title: String,
    val author: String,
    val cover: CoverModel?,
    val language: String,
    val series: String,
    val seriesIndex: String
) : DocumentModel(uuid, format) {

    data class CoverModel(
        val bitmap: Bitmap,
        val mediaType: String
    )

}