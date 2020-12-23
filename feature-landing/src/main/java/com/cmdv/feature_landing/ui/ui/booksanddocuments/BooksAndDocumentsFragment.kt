package com.cmdv.feature_landing.ui.ui.booksanddocuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cmdv.feature_landing.R

class BooksAndDocumentsFragment : Fragment() {

    private val viewModel by viewModels<BooksAndDocumentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_books_and_documents, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        viewModel.allFiles.observe(viewLifecycleOwner, Observer {
            viewModel.fetchEpubBooks(it)
        })
        return root
    }

}