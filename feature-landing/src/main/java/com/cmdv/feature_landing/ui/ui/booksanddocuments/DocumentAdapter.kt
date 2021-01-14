package com.cmdv.feature_landing.ui.ui.booksanddocuments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.utils.getFileSizeFromSizeInKBWithUnit
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.PdfModel
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.feature_landing.R
import com.cmdv.feature_landing.databinding.DocumentItemViewBinding
import java.lang.IllegalStateException

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
            setTitleMain(epub.metadata.titles)
            setTitleSecondary(epub.metadata.titles)
            setAuthor(epub.metadata.creators)
            setType(DocumentType.EPUB)
            setFormat(DocumentType.EPUB)
            setFileSize(epub.fileSize, context)
        }

        private fun showPdf(pdf: PdfModel) {

        }

        private fun setTitleMain(titles: ArrayList<String>) {
            itemBinding.textViewTitleMain.text = titles[0]
        }

        private fun setTitleSecondary(titles: ArrayList<String>) {
            with(itemBinding.textViewTitleSecondary) {
                visibility = View.GONE
                val titleSecondary = if (titles.size > 1) titles[1] else null
                titleSecondary?.let {
                    text = it
                    visibility = View.VISIBLE
                }
            }
        }

        private fun setAuthor(authors: ArrayList<String>?) {
            with(itemBinding.textViewAuthor) {
                visibility = View.GONE
                authors?.let {
                    val author = if (it.size > 0) it[0] else null
                    author?.let {
                        text = author
                        visibility = View.VISIBLE
                    }
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
