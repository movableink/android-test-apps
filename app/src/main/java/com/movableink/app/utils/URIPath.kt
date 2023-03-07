package com.movableink.app.utils

import android.net.Uri

object URIPath {

    fun getProductFromURI(uri: Uri): String? {
        kotlin.runCatching {
            val segments: List<String> = uri.path!!.split("/")
            return segments[segments.size - 1]
        }
        return null
    }
}
