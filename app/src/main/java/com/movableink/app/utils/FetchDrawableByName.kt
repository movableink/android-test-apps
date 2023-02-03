package com.movableink.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.movableink.app.R

@SuppressLint("DiscouragedApi")
fun fetchDrawableByName(name: String, context: Context): Int {
    val resources: Resources = context.resources
    return resources.getIdentifier(
        name.trim(),
        "drawable",
        context.packageName
    )
}

// TODO refactor to this method
fun getDrawableId(name: String): Int? {
    return try {
        val res: Class<*> = R.drawable::class.java
        val idField = res.getField(name)
        idField.getInt(idField)
    } catch (exception: Exception) {
        Log.e("Movable Ink", "Failure to get drawable id.", exception)
    }
}
