package com.movableink.app.salesforce

import android.content.Context
import android.content.Intent

object WebViewUtility {
    fun openUrlInFullScreenWebView(
        context: Context,
        url: String,
    ) {
        val intent =
            Intent(context, FullScreenWebViewActivity::class.java).apply {
                putExtra(FullScreenWebViewActivity.EXTRA_URL, url)
            }
        context.startActivity(intent)
    }
}
