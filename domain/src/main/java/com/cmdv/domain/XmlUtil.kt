package com.cmdv.domain

import android.net.Uri
import android.util.Base64
import android.util.Log
import org.xml.sax.*
import org.xml.sax.helpers.AttributesImpl
import org.xml.sax.helpers.XMLFilterImpl
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

/*
* Functions for processing XML
*/
object XmlUtil {
    private val TAG = XmlUtil::class.java.simpleName

    private val BASE64_DATA_URI: Int = Base64.NO_WRAP

    /*
     * @param uri of the XML document being processed (used to resolve links)
     * @param source to fetch data from
     * @param attrs to update
     * @param attributeName name of attribute to replace value for
     */
    @Throws(IOException::class)
    fun replaceAttributeValueWithDataUri(
        uri: Uri,
//        source: IResourceSource,
        source: Any,
        attrs: Attributes?, attributeName: String?
    ): AttributesImpl {
        val newAttrs = AttributesImpl(attrs)

        // find wanted attribute and update
        for (i in 0 until newAttrs.getLength()) {
            if (newAttrs.getLocalName(i).equals(attributeName)) {
                // if it's already a data URI, nothing to do
                val value: String = newAttrs.getValue(i)
                if (value.length < 5 || value.substring(0, 5) != "data:") {
                    val content: Uri = resolveRelativeUri(uri, value)
//                    TODO
//                    val response: ResourceResponse = source.fetch(content)
//                    if (response != null) {
//                        newAttrs.setValue(i, buildDataUri(response))
//                    }
                }
                break
            }
        }
        return newAttrs
    }

    /*
     * Convert a relative URI into an absolute one
     * @param sourceUri of XML document holding the relative URI
     * @param relativeUri to resolve
     * @return absolute URI
     */
    @Throws(MalformedURLException::class)
    fun resolveRelativeUri(sourceUri: Uri, relativeUri: String?): Uri {
        val source = URL(sourceUri.toString())
        val absolute = URL(source, relativeUri)
        return Uri.parse(absolute.toString())
    }

    @Throws(IOException::class)
//    fun buildDataUri(response: ResourceResponse): String { TODO
    fun buildDataUri(response: Any): String {
        val sb = StringBuilder("data:")
//        TODO
//        sb.append(response.getMimeType())
//        sb.append(";base64,")
//        streamToBase64(response.getData(), sb)
        return sb.toString()
    }

    @Throws(IOException::class)
    fun streamToBase64(`in`: InputStream, sb: StringBuilder) {
        val buflen = 4096
        val buffer = ByteArray(buflen)
        var offset = 0
        var len = 0
        while (len != -1) {
            len = `in`.read(buffer, offset, buffer.size - offset)
            if (len != -1) {
                // must process a multiple of 3 bytes, so that no padding chars
                // are placed
                val total = offset + len
                offset = total % 3
                val bytesToProcess = total - offset
                if (0 < bytesToProcess) {
                    sb.append(Base64.encodeToString(buffer, 0, bytesToProcess, BASE64_DATA_URI))
                }
                // shuffle unused bytes to start of array
                System.arraycopy(buffer, bytesToProcess, buffer, 0, offset)
            } else if (0 < offset) {
                // flush
                sb.append(Base64.encodeToString(buffer, 0, offset, BASE64_DATA_URI))
            }
        }
        `in`.close()
    }

    /*
     * Parse an XML file in the zip.
     *  @fileName name of XML file in the zip
     *  @root parser to read the XML file
     */
    fun parseXmlResource(inputStream: InputStream?, handler: ContentHandler?, lastFilter: XMLFilterImpl?) {
        if (inputStream != null) {
            try {
                val parseFactory: SAXParserFactory = SAXParserFactory.newInstance()
                val reader: XMLReader = parseFactory.newSAXParser().xmlReader
                reader.contentHandler = handler
                inputStream.use {
                    val source = InputSource(it)
                    source.encoding = "UTF-8"
                    if (lastFilter != null) {
                        // this is a chain of filters, setup the pipeline
                        (handler as XMLFilterImpl).parent = reader
                        lastFilter.parse(source)
                    } else {
                        // simple content handler
                        reader.parse(source)
                    }
                }
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: IOException) {
                Log.e(TAG, "Error reading XML file ", e)
            } catch (e: SAXException) {
                Log.e(TAG, "Error parsing XML file ", e)
            }
        }
    }

    /*
     * @param attrs attributes to look through
     * @param name of attribute to get value of
     * @return value of requested attribute, or null if not found
     */
    fun getAttributesValue(attrs: Attributes, name: String?): String? {
        for (i in 0 until attrs.length) {
            if (attrs.getLocalName(i) == name) {
                return attrs.getValue(i)
            }
        }
        return null
    }
}