package com.cmdv.feature_document_detail.ui.fragments

import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.cmdv.core.Constants
import com.cmdv.core.utils.BlurTransformation
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.feature_document_detail.databinding.FragmentEpubDetailBinding
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import org.koin.android.ext.android.inject

class EpubDetailFragment : Fragment() {
    private lateinit var binding: FragmentEpubDetailBinding
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEpubDetailBinding.inflate(inflater, container, false)

        if (this::epub.isInitialized) {
            Toast.makeText(activity, epub.title, Toast.LENGTH_SHORT).show()
            setBackground()
            setCover()
        }

        return binding.root
    }

    private fun setBackground() {
        epub.cover?.bitmap?.run {
            Glide.with(requireActivity())
                .load(this)
                .transform(BlurTransformation(requireContext()))
                .into(binding.imageViewBackground)
        }
    }

    private fun setCover() {
        epub.cover?.bitmap?.run {
            Glide.with(requireActivity())
                .load(this)
                .into(binding.imageViewCover)
        }
    }

}