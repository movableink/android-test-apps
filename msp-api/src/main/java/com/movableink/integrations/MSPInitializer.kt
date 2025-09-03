package com.movableink.integrations

import android.app.Activity
import android.app.Application

interface MSPInitializer {
    fun initialize(application: Application)

    fun onActivityResumed(activity: Activity)

    fun onActivityCreated(activity: Activity)

//    fun logEvent(event: String, properties: Map<String, Any>? = null)
    fun deinitialize()
}
