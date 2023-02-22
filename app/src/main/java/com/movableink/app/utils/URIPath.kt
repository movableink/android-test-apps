package com.movableink.app.utils

import android.net.Uri

object URIPath {

    fun getProductFromURI(uri: Uri): String {
        val segments: List<String> = uri.path!!.split("/")
        return segments[segments.size - 1]
    }
}
