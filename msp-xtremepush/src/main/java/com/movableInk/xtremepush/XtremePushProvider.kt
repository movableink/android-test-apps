package com.movableInk.xtremepush

import android.app.Activity
import android.app.Application
import android.util.Log
import com.movableink.integrations.MSPInitializer
import ie.imobile.extremepush.NativeInappListener
import ie.imobile.extremepush.PushConnector
import ie.imobile.extremepush.PushConnector.mPushConnector

class XtremePushProvider : MSPInitializer {
    override fun initialize(application: Application) {
        try {
            PushConnector
                .Builder("AyqT8EB0wALE2ks_-DwpFO43-CxtGPeI", "957444462700")
                .setServerUrl("https://sdk.fanatics.xtremepush.com")
                .setEnableStartSession(true)
                .turnOnDebugLogs(true)
                .setEnableInApp(true)
                .setServerExpectedPublicKey(
                    @Suppress("ktlint:standard:max-line-length")
                    "3059301306072a8648ce3d020106082a8648ce3d030107034200046b1f4d44e82ff6f9baaa8214ec2a3a731722b3ca3f5c0f5ea5badadf3d039e3e4b1fdafa388486272645e331717b4e962cfacdf4215fcbe5e2900368763ad130",
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
            Log.d("Xtremepush", "In-app message received: ${p0?.data}")
        }
}
