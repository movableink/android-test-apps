package com.movableink.app.utils

import android.net.Uri

object URIPath {

    fun getProductFromURI(uri: Uri): String? {
        kotlin.runCatching {
            val path = uri.path ?: return null
            val segments = path.split("/")
            if (segments.size < 2 || segments[1] != "product") {
                return null
            }
            return segments[2]
        }
        return null
    }
}
