package com.cmdv.domain.models.epub

import org.w3c.dom.Attr

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
    val meta: ArrayList<MetaItemModel>?
)

data class DcModel(
    val dcItems: ArrayList<DcItemModel>?
)

data class DcItemModel(
    val tagName: String,
    val value: String,
    val attributes: ArrayList<Attr>?
)


data class MetaItemModel(
    val content: String,
    val name: String
)