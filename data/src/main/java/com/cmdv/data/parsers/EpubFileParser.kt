package com.cmdv.data.parsers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.sax.Element
import android.sax.RootElement
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import com.cmdv.data.entity.FileEntity
import com.cmdv.data.entity.epub.EpubEntity
import com.cmdv.data.parsers.metadata.MetadataParser
import com.cmdv.domain.HrefResolver
import com.cmdv.domain.XmlUtil
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.epub.TableOfContents
import org.xml.sax.ContentHandler
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


class EpubFileParser : FileParser {

    @Suppress("PrivatePropertyName")
    private val TAG = EpubFileParser::class.java.simpleName

    companion object {
        // the container XML
        private const val XML_PATH_CONTAINER = "META-INF/container.xml"
        private const val XML_NAMESPACE_CONTAINER = "urn:oasis:names:tc:opendocument:xmlns:container"
        private const val XML_ELEMENT_CONTAINER = "container"
        private const val XML_ELEMENT_ROOT_FILES = "rootfiles"
        private const val XML_ELEMENT_ROOT_FILE = "rootfile"
        private const val XML_ATTRIBUTE_FULL_PATH = "full-path"

        // the .opf XML
        private const val XML_NAMESPACE_PACKAGE = "http://www.idpf.org/2007/opf"
        private const val XML_ELEMENT_PACKAGE = "package"
        private const val XML_ATTRIBUTE_VERSION = "version"
        private const val XML_ELEMENT_MANIFEST = "manifest"
        private const val XML_ELEMENT_MANIFEST_ITEM = "item"
        private const val XML_ELEMENT_SPINE = "spine"
        private const val XML_ATTRIBUTE_TOC = "toc"
        private const val XML_ELEMENT_ITEM_REF = "itemref"
        private const val XML_ATTRIBUTE_ID_REF = "idref"
        private const val XML_ATTRIBUTE_HREF = "href"
        private const val XML_ATTRIBUTE_ID = "id"
        private const val XML_ATTRIBUTE_MEDIA_TYPE = "media-type"
    }

    private lateinit var zipFile: ZipFile
    private lateinit var packageVersion: String
    private var opfFileName: String? = null
    private var tocId: String? = null
    private val spine: ArrayList<EpubEntity.ManifestItemEntity> = arrayListOf()
    private val tableOfContents: TableOfContents = TableOfContents()
    private lateinit var metadata: EpubEntity.MetadataEntity
    private lateinit var manifest: EpubEntity.ManifestEntity
    private var fileSizeKb: Long = 0

    @Suppress("UNCHECKED_CAST")
    override fun <T> parse(fileName: String): T? {
        val rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) // TODO
        val filePath = rootPath.path + File.separator + fileName

        try {
            zipFile = ZipFile(filePath)
            parseXmlResource(XML_PATH_CONTAINER, constructContainerFileParser())
            setupOpfFile()
            setupMetadata()
            setupTableOfContent()
            setFieSizeKb(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        opfFileName?.let { opfFilename ->
            tocId?.let { tocId ->
                val file = FileEntity(fileName, filePath, DocumentType.EPUB, fileSizeKb)
                val opf = EpubEntity.OpfEntity(opfFilename, packageVersion, metadata, manifest, spine)
                val cover = getCover()

                return EpubEntity(file, cover, opf, tocId, tableOfContents) as T
            } ?: kotlin.run { return null }
        } ?: kotlin.run { return null }
    }

    private fun getCover(): EpubEntity.CoverEntity? {
        val coverMetaItem: EpubEntity.MetadataEntity.MetaItemEntity? = getMetaByName("cover")
        coverMetaItem?.let { metaItem ->
            val coverManifestItem: EpubEntity.ManifestItemEntity? = getManifestItemById(metaItem.content)
            coverManifestItem?.let { manifestItem ->
                return EpubEntity.CoverEntity(
                    getCoverImageBase64(manifestItem.href, manifestItem.mediaType),
                    manifestItem.mediaType
                )
            }
        }
        return null
    }

    private fun getMetaByName(metaName: String): EpubEntity.MetadataEntity.MetaItemEntity? {
        metadata.metaElements.forEach { if (it.name == metaName) return it }
        return null
    }

    private fun getManifestItemById(id: String) : EpubEntity.ManifestItemEntity? =
        manifest.idIndex[id]

    private fun parseXmlResource(fileName: String, handler: ContentHandler) {
        val inputStream: InputStream? = fetchFromZip(fileName)
        inputStream?.let {
            XmlUtil.parseXmlResource(inputStream, handler, null)
        }
    }

    private fun setupOpfFile() {
        opfFileName?.let {
            parseXmlResource(it, constructOpfFileParser())
        }
    }

    private fun setupTableOfContent() {
        tocId?.let { _tocID ->
            var tocManifestItem: EpubEntity.ManifestItemEntity? = null
            for (i in 0 until manifest.items.size) {
                val item = manifest.items[i]
                if (item.id == _tocID) {
                    tocManifestItem = item
                    break
                }
            }
            tocManifestItem?.let { _tocManifestItem ->
                val tocFileName = _tocManifestItem.href
                val resolver = HrefResolver(tocFileName)
                parseXmlResource(tocFileName, tableOfContents.constructTocFileParser(resolver))
            }
        }
    }

    private fun setupMetadata() {
        opfFileName?.let {
            metadata = MetadataParser().parse(packageVersion, fetchFromZip(it))
        }
    }

    private fun setFieSizeKb(filePath: String) {
        fileSizeKb = (File(filePath).length() / 1024)
    }

    private fun constructContainerFileParser(): ContentHandler {
        val root = RootElement(XML_NAMESPACE_CONTAINER, XML_ELEMENT_CONTAINER)
        val rootFilesElement: Element = root.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOT_FILES)
        val rootFileElement: Element = rootFilesElement.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOT_FILE)

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

        val items = arrayListOf<EpubEntity.ManifestItemEntity>()
        val idIndex: HashMap<String, EpubEntity.ManifestItemEntity> = HashMap()
        childManifestItem.apply {
            setStartElementListener { attributes ->
                EpubEntity.ManifestItemEntity(
                    resolver.toAbsolute(attributes.getValue(XML_ATTRIBUTE_HREF)),
                    attributes.getValue(XML_ATTRIBUTE_ID),
                    attributes.getValue(XML_ATTRIBUTE_MEDIA_TYPE)
                ).let {
                    items.add(it)
                    idIndex[it.id] = it
                }
            }
            setEndElementListener {
                manifest = EpubEntity.ManifestEntity(items, idIndex)
            }
        }

        root.setStartElementListener { attributes ->
            packageVersion = attributes.getValue(XML_ATTRIBUTE_VERSION)
        }

        childSpine.setStartElementListener { attributes -> tocId = attributes.getValue(XML_ATTRIBUTE_TOC) }
        childItemRef.setStartElementListener { attributes ->
            val temp = attributes.getValue(XML_ATTRIBUTE_ID_REF)
            temp?.let { manifest.idIndex[temp]?.let { spine.add(it) } }
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

    private fun getCoverImageBase64(imageName: String, mediaType: String): String? {
        val sanitizedImageName = imageName.substringBeforeLast(".").substringAfterLast("/")
        val enumeration = zipFile.entries()
        while (enumeration.hasMoreElements()) {
            var coverFound = false
            val entry = enumeration.nextElement()
            val fileName = entry.name.substringAfterLast("/").substringBeforeLast(".")
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(entry.name)
            val fileExtensionMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
            fileExtensionMimeType?.let {
                if (fileName == sanitizedImageName && it == mediaType) {
                    val inputStream = zipFile.getInputStream(entry)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
                    val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                    coverFound = true
                    return imageString
                }
            }
            if (coverFound) break
        }
        return null
    }

}