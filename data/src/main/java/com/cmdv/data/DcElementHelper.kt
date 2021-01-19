package com.cmdv.data

import com.cmdv.data.entity.epub.EpubEntity

class DcElementHelper() {

    companion object {
        const val DC_IDENTIFIER_ELEMENT = "dc:identifier"
        const val DC_TITLE_ELEMENT = "dc:title"
        const val DC_CREATOR_ELEMENT = "dc:creator"
        const val DC_LANGUAGE_ELEMENT = "dc:language"
    }

    fun getDcElementValueByTagName(metadata: EpubEntity.MetadataEntity, tagName: String): String? {
        metadata.dcElements.forEach { dcItem ->
            if (dcItem.tagName == tagName) return dcItem.value
        }.run { return null }
    }

}