package com.cmdv.domain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import java.io.File
import java.io.IOException


/*
* Assorted utility functions
*/
object Utility {
    const val ERROR_STRING_ID_EXTRA = "ERROR_STRING_ID_EXTRA"
    fun showToast(context: Context?, stringId: Int) {
        val msg = Toast.makeText(context, stringId, Toast.LENGTH_SHORT)
        msg.setGravity(Gravity.CENTER, msg.xOffset / 2, msg.xOffset / 2)
        msg.show()
    }

    fun finishWithError(activity: Activity, stringId: Int) {
        val intent = Intent()
        intent.putExtra(ERROR_STRING_ID_EXTRA, stringId)
        activity.setResult(Activity.RESULT_CANCELED, intent)
        activity.finish()
    }

    fun showErrorToast(context: Context?, intent: Intent?) {
        if (intent != null) {
            val stringId = intent.getIntExtra(ERROR_STRING_ID_EXTRA, 0)
            if (stringId != 0) {
                showToast(context, stringId)
            }
        }
    }

    /*
     * Return path part of a filename
     */
    fun extractPath(fileName: String?): String {
        return try {
            val path = File(fileName).canonicalFile.parent
            // remove leading '/'
            path?.substring(1) ?: ""
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun concatPath(basePath: String?, pathToAdd: String): String {
        var rawPath = "$basePath/$pathToAdd"
        if (basePath == null || basePath.isEmpty() || pathToAdd.startsWith("/")) {
            rawPath = pathToAdd
        }
        return try {
            File(rawPath).canonicalPath.substring(1)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}