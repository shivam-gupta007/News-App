package com.shivamgupta.newsapp.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

object ChromeCustomTabsUtil {
    fun openWebLinkInChromeTab(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
}