package com.cmdv.domain.models.epub

data class MetadataModel(
    val identifiers: ArrayList<String>,
    val titles: ArrayList<String>,
    val languages: ArrayList<String>,
    val creators: ArrayList<String>?,
    val dates: ArrayList<String>?,
    val publisher: String?,
    val subject: String?,
    val description: String?,
    val right: String?,
    val source: String?,
    val format: String?,
    val relation: String?,
    val rights: String?,
    val type: String?,
)