package com.cmdv.feature_landing.ui.ui.currentreading

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmdv.core.bases.BaseFragment
import com.cmdv.feature_landing.R
import java.io.File

class CurrentReadingFragment : BaseFragment<OpenBooksViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_current_reading, container, false)

        activity?.let {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    it.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                } else {
                    true
                }
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(path, "El oro del depredador - Philip Reeve.epub")
                val parcelFileDescriptor = it.contentResolver.openFileDescriptor(Uri.fromFile(File("/storage/emulated/0/Download/El oro del depredador - Philip Reeve.zip")), "r", null)
                if (file.exists()) {

                }
            }
        }

        return root
    }

}