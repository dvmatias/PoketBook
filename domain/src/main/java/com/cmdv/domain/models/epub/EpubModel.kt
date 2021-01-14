package com.cmdv.domain.models.epub

import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType

data class EpubModel(
    val packageVersion: String,
    override val fileName: String,
    override val filePath: String,
    override val fileType: DocumentType,
    override val fileSize: Long,
    val opfFileName: String,
    val tocID: String,
    val metadata: MetadataModel,
    val spine: ArrayList<ManifestItem>,
    val manifest: Manifest,
    val tableOfContents: TableOfContents

) : DocumentModel()
