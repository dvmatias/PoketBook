package com.cmdv.data.mappers

import com.cmdv.data.DcElementHelper
import com.cmdv.data.MetaElementHelper
import com.cmdv.data.entity.FileEntity
import com.cmdv.data.entity.epub.EpubEntity
import com.cmdv.domain.mappers.BaseMapper
import com.cmdv.domain.models.FileModel
import com.cmdv.domain.models.epub.EpubModel

class EpubMapper : BaseMapper<EpubEntity, EpubModel>() {

    override fun transformEntityToModel(e: EpubEntity): EpubModel {
        val uuid: String = DcElementHelper().getDcElementValueByTagName(e.opf.metadata, DcElementHelper.DC_IDENTIFIER_ELEMENT) ?: ""
        val file: FileModel = transformFileEntity(e.file)
        val title: String = DcElementHelper().getDcElementValueByTagName(e.opf.metadata, DcElementHelper.DC_TITLE_ELEMENT) ?: ""
        val author: String = DcElementHelper().getDcElementValueByTagName(e.opf.metadata, DcElementHelper.DC_CREATOR_ELEMENT) ?: ""
        val cover: EpubModel.CoverModel? = transformCoverEntity(e.cover)
        val language: String = DcElementHelper().getDcElementValueByTagName(e.opf.metadata, DcElementHelper.DC_LANGUAGE_ELEMENT) ?: ""
        val series: String = MetaElementHelper().getMetaEntryValueByName(e.opf.metadata, MetaElementHelper.META_CALIBRE_SERIES_NAME) ?: ""
        val seriesIndex: String = MetaElementHelper().getMetaEntryValueByName(e.opf.metadata, MetaElementHelper.META_CALIBRE_SERIES_INDEX_NAME) ?: ""
        val format: String = e.file.type.format

        return EpubModel(uuid, format, file, title, author, cover, language, series, seriesIndex)
    }

    private fun transformCoverEntity(cover: EpubEntity.CoverEntity?): EpubModel.CoverModel? =
        cover?.image?.let {
            return EpubModel.CoverModel(
                cover.image,
                cover.mediaType ?: ""
            )
        }

    private fun transformFileEntity(file: FileEntity) =
        with(file) { FileModel(name, path, type, size) }


}
