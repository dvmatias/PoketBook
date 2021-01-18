package com.cmdv.domain.models.epub

import com.cmdv.domain.HrefResolver
import org.xml.sax.Attributes

private const val XML_ATTRIBUTE_ID = "id"
private const val XML_ATTRIBUTE_HREF = "href"
private const val XML_ATTRIBUTE_MEDIA_TYPE = "media-type"

class ManifestItem(attributes: Attributes, resolver: HrefResolver) {
    val href: String
    val iD: String
    val mediaType: String

    /*
     * Construct from XML
     */
    init {
        href = resolver.toAbsolute(attributes.getValue(XML_ATTRIBUTE_HREF))
        iD = attributes.getValue(XML_ATTRIBUTE_ID)
        mediaType = attributes.getValue(XML_ATTRIBUTE_MEDIA_TYPE)
    }
}