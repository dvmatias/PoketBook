package com.cmdv.core.utils

import android.content.Context

private const val KILO = 1024

fun Long.getFileSizeFromSizeInKBWithUnit(context: Context): String =
    when {
        this > KILO -> "${"%.2f".format(this.toDouble().div(KILO))} MB"
        else -> "$this KB"
    }
