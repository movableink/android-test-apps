package com.movableInk.appsFlyer

import android.app.Activity
import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.movableink.integrations.ApiKeyProvider
import com.movableink.integrations.MSPInitializer

class AppsFlyerProvider : MSPInitializer {
    override fun initialize(application: Application) {
        val apiKey = ApiKeyProvider.getAppsFlyerKey(application)
        AppsFlyerLib.getInstance().init(apiKey.trim(), null, application)
        AppsFlyerLib.getInstance().start(application)
        AppsFlyerLib.getInstance().setDebugLog(true)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity) {
    }

    override fun deinitialize() {
        Log.d("DefaultMSP", "Default MSP Initialized.")
    }
}
