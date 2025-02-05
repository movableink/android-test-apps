package com.movableink.app.salesforce

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class FullScreenWebViewActivity : ComponentActivity() {
    companion object {
        const val EXTRA_URL = "extra_url"
        private const val USE_LEGACY_WEBVIEW = false
    }

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(EXTRA_URL)
        if (url.isNullOrEmpty()) {
            finish()
            return
        }

        if (USE_LEGACY_WEBVIEW) {
            setupLegacyWebView(url)
        } else {
            setupComposeWebView(url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupLegacyWebView(url: String) {
        webView =
            WebView(this).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                }
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        setContentView(webView)
    }

    private fun setupComposeWebView(url: String) {
        setContent {
            FullScreenWebView(url = url)
        }
    }

    override fun onBackPressed() {
        if (::webView.isInitialized && USE_LEGACY_WEBVIEW && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun FullScreenWebView(url: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
    )
}
