package com.cmdv.domain.models.epub

import android.content.Intent
import android.os.Parcelable
import android.sax.*
import com.cmdv.domain.HrefResolver
import org.xml.sax.ContentHandler


class TableOfContents {
    private lateinit var mNavPoints: ArrayList<NavPoint>
    private var mCurrentDepth = 0
    private var mSupportedDepth = 1
    private var mHrefResolver: HrefResolver? = null

    constructor() {
        mNavPoints = ArrayList<NavPoint>()
    }

    /*
     * Unpacks contents from a bundle
     */
    constructor(intent: Intent, key: String?) {
//        mNavPoints = intent.getParcelableArrayListExtra<Parcelable>(key) TODO
    }

    fun add(navPoint: NavPoint) {
        mNavPoints.add(navPoint)
    }

    fun clear() {
        mNavPoints.clear()
    }

    operator fun get(index: Int): NavPoint {
        return mNavPoints[index]
    }

    /*
    * Used to fetch the last item we're building
    */
    val latestPoint: NavPoint
        get() = mNavPoints[mNavPoints.size - 1]

    fun size(): Int {
        return mNavPoints.size
    }

    /*
     * Packs this into an intent
     */
    fun pack(intent: Intent, key: String?) {
        intent.putExtra(key, mNavPoints)
    }

    /*
     * build parser to parse the "Table of Contents" file,
     * @return parser
     */
    fun constructTocFileParser(resolver: HrefResolver?): ContentHandler {
        val root = RootElement(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_NCX)
        val navMap: Element = root.getChild(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_NAVMAP)
        val navPoint: Element = navMap.getChild(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_NAVPOINT)
        mHrefResolver = resolver
        AddNavPointToParser(navPoint)
        return root.contentHandler
    }

    // Build up code to parse a ToC NavPoint
    private fun AddNavPointToParser(navPoint: Element) {
        val navLabel: Element = navPoint.getChild(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_NAVLABEL)
        val text: Element = navLabel.getChild(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_TEXT)
        val content: Element = navPoint.getChild(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_CONTENT)
        navPoint.setStartElementListener(StartElementListener { attributes ->
            add(NavPoint(attributes.getValue(XML_ATTRIBUTE_PLAYORDER)))
            // extend parser to handle another level of nesting if required
            if (mSupportedDepth == ++mCurrentDepth) {
                val child: Element = navPoint.getChild(XML_NAMESPACE_TABLE_OF_CONTENTS, XML_ELEMENT_NAVPOINT)
                AddNavPointToParser(child)
                ++mSupportedDepth
            }
        })
        text.setEndTextElementListener(EndTextElementListener { body -> latestPoint.navLabel =body })
        content.setStartElementListener(StartElementListener { attributes -> latestPoint.content = mHrefResolver?.ToAbsolute(attributes.getValue(XML_ATTRIBUTE_SCR)) })
        navPoint.setEndElementListener(EndElementListener { --mCurrentDepth })
    }

    companion object {
        private const val XML_NAMESPACE_TABLE_OF_CONTENTS = "http://www.daisy.org/z3986/2005/ncx/"
        private const val XML_ELEMENT_NCX = "ncx"
        private const val XML_ELEMENT_NAVMAP = "navMap"
        private const val XML_ELEMENT_NAVPOINT = "navPoint"
        private const val XML_ELEMENT_NAVLABEL = "navLabel"
        private const val XML_ELEMENT_TEXT = "text"
        private const val XML_ELEMENT_CONTENT = "content"
        private const val XML_ATTRIBUTE_PLAYORDER = "playOrder"
        private const val XML_ATTRIBUTE_SCR = "src"
    }
}