package com.movableink.app

import android.app.Application
import android.os.Build
import android.util.Log
import com.braze.Braze
import com.braze.BrazeActivityLifecycleCallbackListener
import com.braze.configuration.BrazeConfig
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.inked.MIClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MIClient.start()
        FirebaseApp.initializeApp(this)
        registerActivityLifecycleCallbacks(
            BrazeActivityLifecycleCallbackListener(
                sessionHandlingEnabled = true,
                registerInAppMessageManager = true
            )
        )
        MIClient.registerDeeplinkDomains(
            listOf("afra.io")
        )
        // set up braze config
        Braze.getInstance(applicationContext).logCustomEvent("Testing")
        setBrazeIdentifiers()
        BrazeInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(applicationContext)

        val brazeConfig = BrazeConfig.Builder()
            .setDefaultNotificationChannelName(getString(R.string.braze_channel_name))
            .build()
        Braze.configure(this, brazeConfig)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Exception while registering FCM token with Braze.", task.exception)
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
}
