package com.movableInk.xtremepush

import android.app.Activity
import android.app.Application
import android.util.Log
import com.movableink.integrations.ApiKeyProvider
import com.movableink.integrations.MSPInitializer
import ie.imobile.extremepush.NativeInappListener
import ie.imobile.extremepush.PushConnector
import ie.imobile.extremepush.PushConnector.mPushConnector

class XtremePushProvider : MSPInitializer {
    override fun initialize(application: Application) {
        try {
            val appKey = ApiKeyProvider.getXtremePushAppKey(application)
            val senderId =ApiKeyProvider.getXtremePushSenderId(application)
            val publicKey =ApiKeyProvider.getXtremePushPublicKey(application)
            PushConnector
                .Builder(appKey, senderId)
                .setServerUrl("https://sdk.fanatics.xtremepush.com")
                .setEnableStartSession(true)
                .turnOnDebugLogs(true)
                .setEnableInApp(true)
                .setServerExpectedPublicKey(
                    publicKey
                ).setNativeInappListener(inAppListener)
                .create(application)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResumed(activity: Activity) {
        mPushConnector.onResume(activity)
    }

    override fun onActivityCreated(activity: Activity) {
        mPushConnector.hitEvent("start")
        val device = mPushConnector.getDeviceInfo(activity)
        try {
            mPushConnector.setUser("droid-user-${device["XPushDeviceID"]}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deinitialize() {
        Log.d("DefaultMSP", "Default MSP Initialized.")
    }

    private val inAppListener =
        NativeInappListener { p0 ->
            Log.d("Xtremepush", "In-app message received: ${p0?.inapp}")
        }
}
