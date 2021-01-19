package com.cmdv.feature_landing.ui.ui.booksanddocuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.core.navigator.Navigator
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.feature_landing.databinding.FragmentBooksAndDocumentsBinding
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class BooksAndDocumentsFragment : Fragment() {

    private val viewModel: BooksAndDocumentsViewModel by viewModel()
    private val binding get() = _binding!!
    private var _binding: FragmentBooksAndDocumentsBinding? = null
    private lateinit var documentAdapter: DocumentAdapter
    private val navigator: Navigator by inject()
    private val gson: Gson by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksAndDocumentsBinding.inflate(inflater, container, false)
        val view = binding.root

        initViews()
        setupRecyclerView()

        viewModel.documentsLiveData.observe(viewLifecycleOwner, { documents ->
            showDocuments(documents)
        })
        viewModel.getDocuments()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            activity?.let { context ->
                documentAdapter = DocumentAdapter(context, documentItemListener)
                adapter = documentAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun showDocuments(documents: List<DocumentModel>) {
        documentAdapter.setItems(documents)
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    /**
     * [DocumentAdapter.DocumentItemListener] interface implementation.
     */
    private val documentItemListener = object : DocumentAdapter.DocumentItemListener {
        override fun onDocumentClick(position: Int) {
            val document = documentAdapter.getItemByPosition(position)
            activity?.let {
//                val bundle = Bundle().apply { putString("EXTRA_DOCUMENT", gson.toJson(document, EpubModel::class.java)) } TODO
                navigator.toDocumentDetailActivity(it, null, null, false)
            }
        }
    }

}