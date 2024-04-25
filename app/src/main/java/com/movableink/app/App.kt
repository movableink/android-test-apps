package com.movableink.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.util.Log
import android.view.Display.DEFAULT_DISPLAY
import androidx.annotation.RequiresApi
import com.appsflyer.AppsFlyerLib
import com.braze.Braze
import com.braze.BrazeActivityLifecycleCallbackListener
import com.braze.configuration.BrazeConfig
import com.braze.support.BrazeLogger
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.inked.MIClient

private const val LOG_TAG: String = "Application"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppsFlyerLib.getInstance().init(getString(R.string.apps_flyer_id).trim(), null, this)
        AppsFlyerLib.getInstance().start(this)
        AppsFlyerLib.getInstance().setDebugLog(true)
        MIClient.start()
        MIClient.registerDeeplinkDomains(
            listOf("afra.io"),
        )
        FirebaseApp.initializeApp(this)
        registerActivityLifecycleCallbacks(
            BrazeActivityLifecycleCallbackListener(
                sessionHandlingEnabled = true,
                registerInAppMessageManager = true,
            ),
        )
        // set up braze config
        Braze.getInstance(applicationContext).logCustomEvent("Testing")
        setBrazeIdentifiers()

//
//        BrazeInAppMessageManager.getInstance().setCustomInAppMessageManagerListener(
//            BrazeListener(this),
//        )

        BrazeInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(applicationContext)

        val brazeConfig = BrazeConfig.Builder()
            .setDefaultNotificationChannelName(getString(R.string.braze_channel_name))
            .build()
        Braze.configure(this, brazeConfig)

        BrazeLogger.logLevel = Log.VERBOSE
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
            if (!task.isSuccessful) {
                Log.w(LOG_TAG, "Exception while registering FCM token with Braze.", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Braze.getInstance(applicationContext).registeredPushToken = token
        }
    }
    private fun setBrazeIdentifiers() {
        val userID = getString(R.string.testUSerId, Build.PRODUCT ?: "unknown")
        Braze.getInstance(this).changeUser(userID)
        Braze.getInstance(this).currentUser?.setFirstName(getString(R.string.testuserName))
        Braze.getInstance(this).currentUser?.setLastName(getString(R.string.testUserLastName))
    }

    private fun Context.displayContext(): Context {
        val manager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val display = manager.getDisplay(DEFAULT_DISPLAY)
        return createDisplayContext(display)
    }
}
