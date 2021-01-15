package com.cmdv.data.parsers.metadata

import com.cmdv.domain.models.epub.MetaItemModel
import com.cmdv.domain.models.epub.MetadataModel
import org.w3c.dom.*
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

private const val ROOT_ELEMENT = "package"
private const val DC_IDENTIFIER_ELEMENT = "dc:identifier"
private const val DC_TITLE_ELEMENT = "dc:title"
private const val DC_LANGUAGE_ELEMENT = "dc:language"
private const val DC_CREATOR_ELEMENT = "dc:creator"
private const val META_ELEMENT = "meta"

class MetadataParser {

    private var builder: DocumentBuilder

    private var identifiers: ArrayList<String> = arrayListOf()
    private var titles: ArrayList<String> = arrayListOf()
    private var languages: ArrayList<String> = arrayListOf()
    private var creators: ArrayList<String> = arrayListOf()
    private val meta: ArrayList<MetaItemModel>? by lazy { arrayListOf() }

    init {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        builder = factory.newDocumentBuilder()
    }

    fun parse(epubVersion: String, opfFileInputStream: InputStream?): MetadataModel? {
        val doc: Document = builder.parse(opfFileInputStream)
        doc.documentElement.normalize()
        val nodes: NodeList = doc.getElementsByTagName(ROOT_ELEMENT).item(0).childNodes

        val metadataNode = getMetadataNode(nodes) ?: return null
        metadataNode.childNodes?.let { childNodes ->
            for (i in 0 until childNodes.length) {
                with(childNodes.item(i)) {
                    when (this.nodeName) {
                        DC_IDENTIFIER_ELEMENT -> getDcPropertyValue(this)?.run { identifiers.add(this) }
                        DC_TITLE_ELEMENT -> getDcPropertyValue(this)?.run { titles.add(this) }
                        DC_LANGUAGE_ELEMENT -> getDcPropertyValue(this)?.run { languages.add(this) }
                        DC_CREATOR_ELEMENT -> getDcPropertyValue(this)?.run { creators.add(this) }
                        META_ELEMENT -> getMetaElement(this as Element)?.run { meta?.add(this) }
                        else -> {
                        }
                    }
                }
            }
        }
        if (!isRequiredInfo()) return null

        return MetadataModel(
            identifiers,
            titles,
            languages,
            creators,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            meta
        )
    }

    private fun getMetadataNode(nodes: NodeList): Node? {
        for (i in 0 until nodes.length) {
            val node = nodes.item(i)
            if (node.nodeName == "metadata") {
                return node
            }
        }
        return null
    }

    private fun getDcPropertyValue(childNode: Node): String? =
        childNode.firstChild.nodeValue

    private fun getMetaElement(element: Element): MetaItemModel? =
        MetaItemModel(
            element.getAttribute("content"),
            element.getAttribute("name")
        )

    private fun isRequiredInfo(): Boolean =
        identifiers.isNotEmpty() && titles.isNotEmpty() && languages.isNotEmpty()

}