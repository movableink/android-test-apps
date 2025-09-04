package com.movableInk.airship

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import com.movableink.integrations.ApiKeyProvider
import com.movableink.integrations.MSPInitializer
import com.urbanairship.AirshipConfigOptions
import com.urbanairship.UAirship
import com.urbanairship.push.notifications.NotificationProvider

class AirShipProvider : MSPInitializer {
    override fun initialize(application: Application) {
        val config =
            AirshipConfigOptions
                .newBuilder()
                .setAppKey(ApiKeyProvider.getAirShipKey(application))
                .setAppSecret(ApiKeyProvider.getAirShipAppSecret(application))
                .setInProduction(false)
                .setSite(AirshipConfigOptions.SITE_US)
                .setNotificationAccentColor(ContextCompat.getColor(application, android.R.color.holo_purple))
                .setNotificationIcon(android.R.drawable.stat_notify_chat)
                .setNotificationChannel(NotificationProvider.DEFAULT_NOTIFICATION_CHANNEL)
                .setUrlAllowList(arrayOf("*"))
                .build()

        UAirship.takeOff(application, config) { airship ->
            airship.pushManager.userNotificationsEnabled = true
            airship.analytics.trackScreen("Airship manually initialized")
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity) {
    }

    override fun deinitialize() {
        Log.d("DefaultMSP", "Default MSP Initialized.")
    }
}
