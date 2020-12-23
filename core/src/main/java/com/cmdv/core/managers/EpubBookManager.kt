package com.cmdv.core.managers

import android.sax.Element
import android.sax.RootElement
import android.util.Log
import com.cmdv.domain.models.EpubModel
import org.xml.sax.ContentHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory


class EpubBookManager(
    private val zipManager: ZipManager
) {

    private val TAG: String = EpubBookManager::class.java.simpleName

    private val XML_NAMESPACE_CONTAINER = "urn:oasis:names:tc:opendocument:xmlns:container"
    private val epubBooks: ArrayList<EpubModel> = arrayListOf()
    private var mOpfFileName: String? = null

    fun getEpubBooks(epubFileNames: List<String>): List<EpubModel> {
        epubFileNames.forEach { fileName ->
            epubBooks.add(getEpubBook(fileName))
        }
        return epubBooks
    }

    private fun getEpubBook(filePath: String?): EpubModel {
        val contentHandler: ContentHandler? = constructContainerFileParser()
        val inputStream: InputStream? = zipManager.getEpubZipFileInputStream(filePath)
        inputStream?.let {
            try {
                inputStream.use {
                    // obtain XML Reader
                    val parseFactory: SAXParserFactory = SAXParserFactory.newInstance()
                    val reader: XMLReader = parseFactory.newSAXParser().xmlReader
                    // connect reader to content handler
                    reader.contentHandler = contentHandler
                    // process XML
                    val source = InputSource(it)
                    source.encoding = "UTF-8"
                    reader.parse(source)
                }
            } catch (e: ParserConfigurationException) {
                Log.e(TAG, "Error setting up to parse XML file ", e)
            } catch (e: IOException) {
                Log.e(TAG, "Error reading XML file ", e)
            } catch (e: SAXException) {
                Log.e(TAG, "Error parsing XML file ", e)
            }
        }

        return EpubModel("") // TODO
    }

    private fun constructContainerFileParser(): ContentHandler? {
        // describe the relationship of the elements
        val root = RootElement(XML_NAMESPACE_CONTAINER, "container")
        val rootfilesElement: Element = root.getChild(XML_NAMESPACE_CONTAINER, "rootfiles")
        val rootfileElement: Element = rootfilesElement.getChild(XML_NAMESPACE_CONTAINER, "rootfile")

        // how to parse a rootFileElement
        rootfileElement.setStartElementListener { attributes ->
            val mediaType = attributes.getValue("media-type")
            if (mediaType != null && mediaType == "application/oebps-package+xml") {
                mOpfFileName = attributes.getValue("full-path")
            }
        }
        return root.contentHandler
    }
}