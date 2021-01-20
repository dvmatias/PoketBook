package com.cmdv.feature_document_detail.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cmdv.core.Constants
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.feature_document_detail.R
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class EpubDetailFragment : Fragment() {
    private lateinit var epub: EpubModel
    private val gson: Gson by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val epubString = it.getString(Constants.ARGUMENT_EPUB_KEY)
            if (!epubString.isNullOrEmpty()) {
                epub = gson.fromJson(epubString, EpubModel::class.java)
            }
        }
        if (this::epub.isInitialized) {
            Toast.makeText(activity, epub.title, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_epub_detail, container, false)
    }

}