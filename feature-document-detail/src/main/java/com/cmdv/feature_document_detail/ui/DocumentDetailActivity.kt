package com.cmdv.feature_document_detail.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cmdv.core.Constants
import com.cmdv.domain.models.DocumentModel
import com.cmdv.domain.models.DocumentType
import com.cmdv.domain.models.epub.EpubModel
import com.cmdv.domain.models.pdf.PdfModel
import com.cmdv.feature_document_detail.R
import com.cmdv.feature_document_detail.databinding.ActivityDocumentDetailBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class DocumentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentDetailBinding
    private val gson: Gson by inject()
    private lateinit var documentClass: Class<*>
    private lateinit var document: DocumentModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(R.id.toolbar))

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        getExtras()
        setNavigation()
        showDocument()

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun getExtras() {
        intent.extras?.let { extras ->
            val format = extras.getString(Constants.EXTRA_DOCUMENT_FORMAT_KEY)
            documentClass = when (format) {
                DocumentType.EPUB.format -> EpubModel::class.java
                DocumentType.PDF.format -> PdfModel::class.java
                else -> throw IllegalStateException("The document you're trying to open has invalid format.")
            }
            document = gson.fromJson(
                extras.getString(Constants.EXTRA_DOCUMENT_KEY),
                documentClass
            ) as DocumentModel
        }
    }

    private fun setNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun showDocument() {
        val bundle = bundleOf(Constants.ARGUMENT_EPUB_KEY to intent.extras?.getString(Constants.EXTRA_DOCUMENT_KEY))
        navController.navigate(R.id.nav_home, bundle)
    }

}