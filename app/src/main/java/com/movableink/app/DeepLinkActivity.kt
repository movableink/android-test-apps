package com.movableink.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.movableink.app.ui.navigation.DeepLinkPattern
import com.movableink.app.utils.URIPath
import com.movableink.inked.MIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("RestrictedApi")
class DeepLinkActivity : ComponentActivity() {
    private val TAG = "DeepLinkActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* // ATTENTION: This was auto-generated to handle app links.
         val appLinkIntent: Intent = intent
         val appLinkAction: String? = appLinkIntent.action
         val appLinkData: Uri? = appLinkIntent.data
         if (appLinkData != null) {
             val uri = this.intent.data
             fetchClickableLink(uri.toString())
         }*/
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
        // Registers the BrazeInAppMessageManager for the current Activity. This Activity will now listen for
        // in-app messages from Braze.
        BrazeInAppMessageManager.getInstance().registerInAppMessageManager(this)
    }
    public override fun onPause() {
        super.onPause()
        // Unregisters the BrazeInAppMessageManager.
        BrazeInAppMessageManager.getInstance().unregisterInAppMessageManager(this)
    }

    private fun fetchClickableLink(uri: String) {
        /* MIClient.resolveUrlAsync(uri) { resolvedLink ->
             resolvedLink?.let {
                 deepLinkToProductPage(it)
             }
         }*/
        lifecycleScope.launch {
            val resolvedLink = withContext(Dispatchers.IO) {
                MIClient.resolveUrl(uri)
            }

            resolvedLink?.let {
                deepLinkToProductPage(it)
            }
        }
    }

    private fun deepLinkToProductPage(url: String) {
        URIPath.getProductFromURI(url.toUri())?.let { productId ->
            val productDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "${DeepLinkPattern.baseDestination}/$productId".toUri(),
                this@DeepLinkActivity,
                MainActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            try {
                startActivity(productDetailIntent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send deep link pending intent", e)
            }
        }
    }
}
