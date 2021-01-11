package com.cmdv.core.managers

import android.sax.Element
import android.sax.RootElement
import android.util.Log
import com.cmdv.domain.models.epub.EpubModel
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


}