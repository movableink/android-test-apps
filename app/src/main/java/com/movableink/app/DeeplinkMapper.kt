package com.movableink.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.movableink.app.ui.navigation.DeepLinkPattern
import com.movableink.app.utils.URIPath

private const val TAG = "DeepLinkMapper"
private const val MISCHEME = "miapp"
private const val HTTPSCHEME = "https"

fun deepLinkToProductPage(url: String, context: Context, scheme: Scheme = Scheme.GLOBAL) {
    URIPath.getProductFromURI(url.toUri(), scheme)?.let { productId ->
        val productDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "${DeepLinkPattern.baseDestination}/$productId".toUri(),
            context,
            MainActivity::class.java,
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        try {
            context.startActivity(productDetailIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send deep link pending intent", e)
        }
    } ?: run {
        /*cannot deeplink to a product with the given click-through , go to homepage */
        with(context) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            val activity = this as? DeepLinkActivity
            activity?. finish()
        }
        Log.e(TAG, "The Product value is null ")
    }
}

fun hasInternalScheme(urlString: String): Boolean {
    val uri = Uri.parse(urlString)
    return uri.scheme == MISCHEME
}
fun hasExternalScheme(urlString: String): Boolean {
    val uri = Uri.parse(urlString)
    return uri.scheme == HTTPSCHEME
}

fun urlScheme(urlString: String): Scheme {
    runCatching {
        if (hasInternalScheme(urlString)) {
            return Scheme.INTERNAL
        }
        if (hasExternalScheme(urlString)) {
            return Scheme.GLOBAL
        }
    }.onFailure { Log.d(TAG, "urlScheme: Malformed") }
    return Scheme.OTHER
}

enum class Scheme {
    INTERNAL,
    GLOBAL,
    OTHER,
}
