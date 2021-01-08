package com.cmdv.domain.models.epub

data class EpubModel(
    val fileName: String,
    val filePath: String,
    val opfFileName: String,
    val tocID: String,
    val spine: ArrayList<ManifestItem>,
    val manifest: Manifest,
    val tableOfContents: TableOfContents
)
