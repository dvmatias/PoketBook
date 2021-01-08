package com.cmdv.data.parsers

import android.os.Environment
import android.sax.Element
import android.sax.RootElement
import android.util.Log
import com.cmdv.domain.HrefResolver
import com.cmdv.domain.XmlUtil
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.epub.Manifest
import com.cmdv.domain.models.epub.ManifestItem
import com.cmdv.domain.models.epub.TableOfContents
import org.xml.sax.ContentHandler
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


class EpubFileParser : FileParser {

    private val TAG = EpubFileParser::class.java.simpleName

    companion object {
        private const val HTTP_SCHEME = "http"

        // the container XML
        private const val XML_PATH_CONTAINER = "META-INF/container.xml"
        private const val XML_NAMESPACE_CONTAINER = "urn:oasis:names:tc:opendocument:xmlns:container"
        private const val XML_ELEMENT_CONTAINER = "container"
        private const val XML_ELEMENT_ROOT_FILES = "rootfiles"
        private const val XML_ELEMENT_ROOT_FILE = "rootfile"
        private const val XML_ATTRIBUTE_FULL_PATH = "full-path"
        private const val XML_ATTRIBUTE_MEDIA_TYPE = "media-type"

        // the .opf XML
        private const val XML_NAMESPACE_PACKAGE = "http://www.idpf.org/2007/opf"
        private const val XML_ELEMENT_PACKAGE = "package"
        private const val XML_ELEMENT_MANIFEST = "manifest"
        private const val XML_ELEMENT_MANIFEST_ITEM = "item"
        private const val XML_ELEMENT_SPINE = "spine"
        private const val XML_ATTRIBUTE_TOC = "toc"
        private const val XML_ELEMENT_ITEM_REF = "itemref"
        private const val XML_ATTRIBUTE_ID_REF = "idref"
    }

    // Zip file
    private lateinit var zipFile: ZipFile
    // OPF file name
    private var opfFileName: String? = null
    // TOC ID (table of content)
    private var tocID: String? = null
    // The resources that are in the spine element of the metadata.
    private val spine: ArrayList<ManifestItem> = arrayListOf()
    // The manifest entry in the metadata.
    private val manifest: Manifest = Manifest()
    // The Table of Contents in the metadata.
    private val mTableOfContents: TableOfContents = TableOfContents()

    @Suppress("UNCHECKED_CAST")
    override fun <T> parse(fileName: String): T? {
        val rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) // TODO
        val filePath = rootPath.path + File.separator + fileName

        try {
            zipFile = ZipFile(filePath)
            // get the "container" file, this tells us where the ".opf" file is
            parseXmlResource(XML_PATH_CONTAINER, constructContainerFileParser())

            opfFileName?.let { parseXmlResource(it, constructOpfFileParser()) }

            if (tocID != null) {
                val tocManifestItem = manifest.findById(tocID!!)
                if (tocManifestItem != null) {
                    val tocFileName = tocManifestItem.href
                    val resolver = HrefResolver(tocFileName)
                    parseXmlResource(tocFileName, mTableOfContents.constructTocFileParser(resolver))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return if (opfFileName != null && tocID != null) {
            EpubModel(fileName, filePath, opfFileName!!, tocID!!, spine, manifest, mTableOfContents) as T
        } else {
            null
        }
    }

    private fun parseXmlResource(fileName: String, handler: ContentHandler) {
        val inputStream: InputStream? = fetchFromZip(fileName)
        inputStream?.let {
            XmlUtil.parseXmlResource(inputStream, handler, null)
        }
    }

    private fun constructContainerFileParser(): ContentHandler {
        // describe the relationship of the elements
        val root = RootElement(XML_NAMESPACE_CONTAINER, XML_ELEMENT_CONTAINER)
        val rootFilesElement: Element = root.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOT_FILES)
        val rootFileElement: Element = rootFilesElement.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOT_FILE)

        // how to parse a rootFileElement
        rootFileElement.setStartElementListener { attributes ->
            val mediaType = attributes.getValue(XML_ATTRIBUTE_MEDIA_TYPE)
            if (mediaType != null && mediaType == "application/oebps-package+xml") {
                opfFileName = attributes.getValue(XML_ATTRIBUTE_FULL_PATH)
            }
        }
        return root.contentHandler
    }

    /*
     * build parser to parse the ".opf" file,
     * @return parser
     */
    private fun constructOpfFileParser(): ContentHandler {
        // describe the relationship of the elements
        val root = RootElement(XML_NAMESPACE_PACKAGE, XML_ELEMENT_PACKAGE)
        val childManifest = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_MANIFEST)
        val childManifestItem = childManifest.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_MANIFEST_ITEM)
        val childSpine = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_SPINE)
        val childItemRef = childSpine.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_ITEM_REF)
        val resolver = HrefResolver(opfFileName)
        childManifestItem.setStartElementListener { attributes ->
            manifest.add(ManifestItem(attributes, resolver))
        }

        // get name of Table of Contents file from the Spine
        childSpine.setStartElementListener { attributes -> tocID = attributes.getValue(XML_ATTRIBUTE_TOC) }
        childItemRef.setStartElementListener { attributes ->
            val temp = attributes.getValue(XML_ATTRIBUTE_ID_REF)
            temp?.let {
                val item = manifest.findById(it)
                item?.let { spine.add(item) }
            }
        }
        return root.contentHandler
    }

    /*
     * Fetch file from zip
     */
    private fun fetchFromZip(fileName: String): InputStream? {
        var inputStream: InputStream? = null
        val containerEntry: ZipEntry? = zipFile.getEntry(fileName)
        containerEntry?.let {
            try {
                inputStream = zipFile.getInputStream(it)
            } catch (e: IOException) {
                Log.e(TAG, "Error reading zip file $fileName", e)
            }
        }
        inputStream?.let { } ?: Log.e(TAG, "Unable to find file in zip: $fileName")
        return inputStream
    }

}