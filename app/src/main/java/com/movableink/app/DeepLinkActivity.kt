package com.movableink.app

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ComponentActivity
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.movableink.app.ui.navigation.DeepLinkPattern
import com.movableink.app.utils.URIPath
import com.movableink.inked.network.MIClient
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
class DeepLinkActivity : ComponentActivity() {
    private val TAG = "DeepLinkActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
        if (appLinkData != null) {
            val uri = this.intent.data
            fetchClickableLink(uri.toString())
        }
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
        lifecycleScope.launch {
            val resolvedLink = MIClient.resolveUrl(uri)
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
            )
            TaskStackBuilder.create(this@DeepLinkActivity).run {
                addNextIntentWithParentStack(productDetailIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    ?.send() ?: throw IllegalStateException("Failed to send deep link pending intent is null")
            }
        }
    }
}
