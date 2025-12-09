package com.movableink.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.util.Log
import android.view.Display.DEFAULT_DISPLAY
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.inked.MIClient
import com.movableink.integrations.MSPManager

private const val LOG_TAG: String = "Application"

class App : Application() {
    @Suppress("ktlint:standard:no-consecutive-comments")
    override fun onCreate() {
        super.onCreate()
        MIClient.start()
        MIClient.registerDeeplinkDomains(
            listOf("afra.io"),
        )
        MIClient.appInstallEventEnabled(true)
        FirebaseApp.initializeApp(this)
        @Suppress("ktlint:standard:property-naming")
        val SELECTED_MSP_KEY = stringPreferencesKey("selected_msp")
        createChanel()
        registerToken()
        MSPManager.initialize(this)
    }

    private fun Context.displayContext(): Context {
        val manager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val display = manager.getDisplay(DEFAULT_DISPLAY)
        return createDisplayContext(display)
    }

    private fun createChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("my_channel_01", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun registerToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
            },
        )
    }
}
