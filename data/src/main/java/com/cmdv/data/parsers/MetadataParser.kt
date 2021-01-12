package com.cmdv.data.parsers

import com.cmdv.domain.models.epub.MetadataModel
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

private const val ROOT_ELEMENT = "package"
private const val IDENTIFIER_ELEMENT = "dc:identifier"
private const val TITLE_ELEMENT = "dc:title"
private const val LANGUAGE_ELEMENT = "dc:language"

class MetadataParser {

    private var builder: DocumentBuilder

    private var identifiers: ArrayList<String> = arrayListOf()
    private var titles: ArrayList<String> = arrayListOf()
    private var languages: ArrayList<String> = arrayListOf()

    init {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        builder = factory.newDocumentBuilder()
    }

    fun parse(opfFileInputStream: InputStream?): MetadataModel? {
        val doc: Document = builder.parse(opfFileInputStream)
        doc.documentElement.normalize()
        val nodes: NodeList = doc.getElementsByTagName(ROOT_ELEMENT).item(0).childNodes

        val metadataNode = getMetadataNode(nodes) ?: return null
        metadataNode.childNodes?.let { childNodes ->
            for (i in 0 until childNodes.length) {
                with(childNodes.item(i)) {
                    when (this.nodeName) {
                        IDENTIFIER_ELEMENT -> getPropertyValue(this)?.run { identifiers.add(this) }
                        TITLE_ELEMENT -> getPropertyValue(this)?.run { titles.add(this) }
                        LANGUAGE_ELEMENT -> getPropertyValue(this)?.run { languages.add(this) }
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
            null,
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

    private fun getPropertyValue(childNode: Node): String? =
        childNode.firstChild.nodeValue

    private fun isRequiredInfo(): Boolean =
        identifiers.isNotEmpty() && titles.isNotEmpty() && languages.isNotEmpty()

}