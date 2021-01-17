package com.cmdv.feature_landing.ui.ui.booksanddocuments

import android.content.Context
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

class DocumentAdapter(private val context: Context) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

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
        holder.bindView(documents[position], context)
    }

    override fun getItemCount(): Int =
        documents.size

    class DocumentViewHolder(private val itemBinding: DocumentItemViewBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindView(document: DocumentModel, context: Context) {
            when (document.fileType) {
                DocumentType.EPUB -> showEpub(document as EpubModel, context)
                DocumentType.PDF -> showPdf(document as PdfModel)
                else -> throw IllegalStateException("A document must have a valid format.")
            }
        }

        private fun showEpub(epub: EpubModel, context: Context) {
            setTitle(epub.title)
            setSeries(epub.series)
            setSeriesIndex(epub.seriesIndex)
            setAuthor(epub.author)
            setType(DocumentType.EPUB)
            setFormat(DocumentType.EPUB)
            setFileSize(epub.file.size, context)
        }

        private fun showPdf(pdf: PdfModel) {

        }

        private fun setTitle(title: String) {
            itemBinding.textViewTitle.text = title
        }

        private fun setSeries(series: String) {
            with(itemBinding.textViewSeries) {
                if (series.isNotEmpty()) {
                    text = series
                    visibility = View.VISIBLE
                }
            }
        }

        private fun setSeriesIndex(seriesIndex: String) {
            with(itemBinding.textViewSeriesIndex) {
                if (seriesIndex.isNotEmpty()) {
                    text = seriesIndex
                    visibility = View.VISIBLE
                }
            }
        }

        private fun setAuthor(author: String?) {
            with(itemBinding.textViewAuthor) {
                visibility = View.GONE
                author?.let {
                    text = author
                    visibility = View.VISIBLE
                }
            }
        }

        private fun setType(documentType: DocumentType) {
            itemBinding.imageViewType.setImageResource(
                when (documentType) {
                    DocumentType.EPUB -> R.drawable.ic_menu_camera
                    DocumentType.PDF -> R.drawable.ic_menu_gallery
                    else -> throw IllegalStateException("A document must have a valid format.")
                }
            )
        }

        private fun setFormat(documentType: DocumentType) {
            itemBinding.textViewFormat.text =
                when (documentType) {
                    DocumentType.EPUB -> "EPUB"
                    DocumentType.PDF -> "PDF"
                    else -> throw IllegalStateException("A document must have a valid format.")
                }
        }

        private fun setFileSize(fileSize: Long, context: Context) {
            itemBinding.textViewSize.text = fileSize.getFileSizeFromSizeInKBWithUnit(context)
        }
    }

}
