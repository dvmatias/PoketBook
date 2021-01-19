package com.cmdv.data

import com.cmdv.data.entity.epub.EpubEntity

class MetaElementHelper {

    companion object {
        const val META_CALIBRE_SERIES_NAME = "calibre:series"
        const val META_CALIBRE_SERIES_INDEX_NAME = "calibre:series_index"
    }

    fun getMetaEntryValueByName(metadata: EpubEntity.MetadataEntity, name: String): String? =
        metadata.metaElements.forEach { metaItem ->
            if (metaItem.name == name) return metaItem.content
        }.run { return null }

}