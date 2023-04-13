package com.movableink.app.utils

import android.net.Uri
import com.movableink.app.Scheme

object URIPath {

    fun getProductFromURI(uri: Uri, scheme: Scheme): String? {
        kotlin.runCatching {
            val path = uri.path ?: return null
            val segments = path.split("/")
            when (scheme) {
                Scheme.INTERNAL -> {
                    if (segments.size < 2) return null
                    val productIdParam = segments[1].split("?").firstOrNull()
                    return productIdParam?.toIntOrNull()?.toString()
                }
                Scheme.GLOBAL -> {
                    if (segments.size < 3) return null
                    return segments[2]
                }

                else -> { return@runCatching }
            }
        }
        return null
    }
}
