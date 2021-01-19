package com.cmdv.feature_landing.ui.ui.booksanddocuments

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.utils.getFileSizeFromSizeInKBWithUnit
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.pdf.PdfModel
import com.cmdv.feature_landing.R
import com.cmdv.feature_landing.databinding.DocumentItemViewBinding


class DocumentAdapter(
    private val context: Context,
    private val listener: DocumentItemListener
) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    private val documents = arrayListOf<DocumentModel>()

    fun setItems(items: List<DocumentModel>) {
        documents.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val itemBinding = DocumentItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bindView(documents[position], context, listener, position)
    }

    override fun getItemCount(): Int =
        documents.size

    fun getItemByPosition(position: Int) = documents[position]

    class DocumentViewHolder(private val itemBinding: DocumentItemViewBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private lateinit var context: Context
        private lateinit var listener: DocumentItemListener

        fun bindView(document: DocumentModel, context: Context, listener: DocumentItemListener, position: Int) {
            this.context = context
            this.listener = listener

            when (document.fileType) {
                DocumentType.EPUB -> setEpub(document as EpubModel, position)
                DocumentType.PDF -> showPdf(document as PdfModel)
                else -> throw IllegalStateException("A document must have a valid format.")
            }
        }

        private fun setEpub(epub: EpubModel, position: Int) {
            showCover(epub.cover.image)
            showTitle(epub.title)
            showSeries(epub.series)
            showSeriesIndex(epub.seriesIndex)
            showAuthor(epub.author)
            showType(DocumentType.EPUB)
            showFormat(DocumentType.EPUB)
            showFileSize(epub.file.size, context)
            itemBinding.cardViewDocumentContainer.setOnClickListener {
                listener.onDocumentClick(position)
            }
        }

        private fun showPdf(pdf: PdfModel) {

        }

        private fun showCover(cover: String) {
            if (cover.isEmpty()) {
                TODO()
            } else {
                val coverBytes = Base64.decode(cover, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.size)
                itemBinding.imageViewCover.setImageBitmap(decodedImage)
            }
        }

        private fun showTitle(title: String) {
            itemBinding.textViewTitle.text = title
        }

        private fun showSeries(series: String) {
            with(itemBinding.textViewSeries) {
                if (series.isNotEmpty()) {
                    text = series
                    visibility = View.VISIBLE
                }
            }
        }

        private fun showSeriesIndex(seriesIndex: String) {
            with(itemBinding.textViewSeriesIndex) {
                if (seriesIndex.isNotEmpty()) {
                    text = seriesIndex
                    visibility = View.VISIBLE
                }
            }
        }

        private fun showAuthor(author: String?) {
            with(itemBinding.textViewAuthor) {
                visibility = View.GONE
                author?.let {
                    text = author
                    visibility = View.VISIBLE
                }
            }
        }

        private fun showType(documentType: DocumentType) {
            itemBinding.imageViewType.setImageResource(
                when (documentType) {
                    DocumentType.EPUB -> R.drawable.ic_epub_24dp
                    DocumentType.PDF -> R.drawable.ic_menu_gallery
                    else -> throw IllegalStateException("A document must have a valid format.")
                }
            )
        }

        private fun showFormat(documentType: DocumentType) {
            itemBinding.textViewFormat.text =
                when (documentType) {
                    DocumentType.EPUB -> "EPUB"
                    DocumentType.PDF -> "PDF"
                    else -> throw IllegalStateException("A document must have a valid format.")
                }
        }

        private fun showFileSize(fileSize: Long, context: Context) {
            itemBinding.textViewSize.text = fileSize.getFileSizeFromSizeInKBWithUnit(context)
        }
    }

    interface DocumentItemListener {
        fun onDocumentClick(position: Int)
    }

}