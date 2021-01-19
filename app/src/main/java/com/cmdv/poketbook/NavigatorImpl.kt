package com.cmdv.poketbook

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.cmdv.core.extensions.navigate
import com.cmdv.core.navigator.Navigator
import com.cmdv.feature_document_detail.ui.DocumentDetailActivity

class NavigatorImpl : Navigator {

    override fun toDocumentDetailActivity(activityOrigin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean) {
        activityOrigin.navigate<DocumentDetailActivity>(bundle, options, finish)
    }
}