package com.cmdv.data.entity.epub

import android.graphics.Bitmap
import com.cmdv.data.entity.FileEntity
import com.cmdv.domain.models.epub.TableOfContents
import org.w3c.dom.Attr

data class EpubEntity(
    val file: FileEntity,
    val cover: CoverEntity?,
    val opf: OpfEntity,
    val tocId: String,
    val tableOfContents: TableOfContents
) {
    data class CoverEntity(
        val image: Bitmap?,
        val mediaType: String?
    )

    data class OpfEntity(
        val opfFileName: String,
        val packageVersion: String,
        val metadata: MetadataEntity,
        val manifest: ManifestEntity,
        val spine: ArrayList<ManifestItemEntity>
    )

    data class MetadataEntity(
        val dcElements: ArrayList<DcItemEntity>,
        val metaElements: ArrayList<MetaItemEntity>
    ) {
        data class DcItemEntity(
            val tagName: String,
            val value: String,
            val attributes: ArrayList<Attr>?
        )

        data class MetaItemEntity(
            val content: String,
            val name: String
        )
    }

    data class ManifestEntity(
        val items: ArrayList<ManifestItemEntity>,
        val idIndex: HashMap<String, ManifestItemEntity>
    )

    data class ManifestItemEntity(
        val href: String,
        val id: String,
        val mediaType: String
    )

}