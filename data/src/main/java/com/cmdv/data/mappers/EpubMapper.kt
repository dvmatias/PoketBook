package com.cmdv.data.mappers

import com.cmdv.data.entity.FileEntity
import com.cmdv.data.entity.epub.EpubEntity
import com.cmdv.domain.mappers.BaseMapper
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.FileModel
import com.cmdv.domain.models.epub.EpubModel

class EpubMapper : BaseMapper<EpubEntity, EpubModel>() {

    override fun transformEntityToModel(e: EpubEntity): EpubModel {
        val file: FileModel = transformFileEntity(e.file)
        val title: String = getDcEntryValue(e.opf.metadata, "dc:title") ?: ""
        val author: String = getDcEntryValue(e.opf.metadata, "dc:creator") ?: ""
        val cover: EpubModel.CoverModel = transformCoverEntity(e.cover)
        val language: String = getDcEntryValue(e.opf.metadata, "dc:language") ?: ""
        val series: String = getMetaEntryValue(e.opf.metadata, "calibre:series") ?: ""
        val seriesIndex: String = getMetaEntryValue(e.opf.metadata, "calibre:series_index") ?: ""
        val format: String = if (e.file.type == DocumentType.EPUB) "EPUB" else "PDF"

        return EpubModel(e.file.type, file, title, author, cover, language, series, seriesIndex, format)
    }

    private fun transformCoverEntity(cover: EpubEntity.CoverEntity?): EpubModel.CoverModel =
        EpubModel.CoverModel(
            cover?.image ?: "",
            cover?.mediaType ?: ""
        )

    private fun transformFileEntity(file: FileEntity) =
        with(file) { FileModel(name, path, type, size) }

    private fun getDcEntryValue(metadata: EpubEntity.MetadataEntity, tagName: String): String? {
        var value: String? = null
        for (element in metadata.dcElements) {
            if (element.tagName == tagName) {
                value = element.value
                break
            }
        }
        return value
    }

    private fun getMetaEntryValue(metadata: EpubEntity.MetadataEntity, name: String): String? {
        var value: String? = null
        for (element in metadata.metaElements) {
            if (element.name == name) {
                value = element.content
                break
            }
        }
        return value
    }

}
