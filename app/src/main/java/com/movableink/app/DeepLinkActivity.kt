package com.movableink.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.movableink.inked.MIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("RestrictedApi")
class DeepLinkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_VIEW) {
                val url = it.data.toString()
                fetchClickableLink(url)
            }
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    public override fun onResume() {
        super.onResume()
    }
    public override fun onPause() {
        super.onPause()
    }

    private fun fetchClickableLink(uri: String) {
        /* MIClient.resolveUrlAsync(uri) { resolvedLink ->
             resolvedLink?.let {
                 deepLinkToProductPage(it)
             }
         }*/

        when (urlScheme(uri)) {
            Scheme.INTERNAL -> {
                deepLinkToProductPage(uri, this@DeepLinkActivity, Scheme.INTERNAL)
            }
            Scheme.GLOBAL -> {
                lifecycleScope.launch {
                    val resolvedLink = withContext(Dispatchers.IO) {
                        MIClient.resolveUrl(uri)
                    }
                    resolvedLink?.let {
                        deepLinkToProductPage(it, this@DeepLinkActivity)
                    }
                }
            }
            Scheme.OTHER -> {
                return
            }
        }
    }
}
