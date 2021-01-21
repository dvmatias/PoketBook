package com.cmdv.data.parsers.metadata

import com.cmdv.data.entity.epub.EpubEntity
import com.cmdv.domain.models.epub.MetaItemModel
import org.w3c.dom.*
import java.io.InputStream
import java.lang.Exception
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

private const val ROOT_ELEMENT = "package"
private const val DC_IDENTIFIER_ELEMENT = "dc:identifier"
private const val DC_TITLE_ELEMENT = "dc:title"
private const val DC_LANGUAGE_ELEMENT = "dc:language"
private const val DC_CREATOR_ELEMENT = "dc:creator"
private const val META_ELEMENT = "meta"
private const val META_CONTENT = "content"
private const val META_NAME = "name"

class MetadataParser {

    private var builder: DocumentBuilder
    private val dcElements: ArrayList<EpubEntity.MetadataEntity.DcItemEntity> = arrayListOf()
    private val metaElements: ArrayList<EpubEntity.MetadataEntity.MetaItemEntity> = arrayListOf()

    init {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        builder = factory.newDocumentBuilder()
    }

    fun parse(epubVersion: String, opfFileInputStream: InputStream?): EpubEntity.MetadataEntity {
        val doc: Document = builder.parse(opfFileInputStream)
        doc.documentElement.normalize()
        val nodes: NodeList = doc.getElementsByTagName(ROOT_ELEMENT).item(0).childNodes

        val metadataNodes = getMetadataChildNodes(nodes) ?: throw IllegalStateException("Metadata node can't be null.")
        setDcElements(metadataNodes)
        setMetaElements(metadataNodes)

        return EpubEntity.MetadataEntity(dcElements, metaElements)
    }

    private fun setDcElements(metadataNodes: NodeList) {
        for (i in 0 until metadataNodes.length) {
            try {
                with(metadataNodes.item(i) as Element) {
                    dcElements.add(EpubEntity.MetadataEntity.DcItemEntity(tagName, firstChild.nodeValue, null))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setMetaElements(metadataNodes: NodeList) {
        for (i in 0 until metadataNodes.length) {
            try {
                if (metadataNodes.item(i) is Element) {
                    with(metadataNodes.item(i) as Element) {
                        if (tagName == META_ELEMENT) {
                            metaElements.add(EpubEntity.MetadataEntity.MetaItemEntity(getAttribute(META_CONTENT), getAttribute(META_NAME)))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getMetadataChildNodes(nodes: NodeList): NodeList? {
        for (i in 0 until nodes.length) {
            val node = nodes.item(i)
            if (node.nodeName == "metadata") {
                return node.childNodes
            }
        }
        return null
    }

}