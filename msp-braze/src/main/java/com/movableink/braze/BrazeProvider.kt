package com.movableink.braze

import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import com.braze.Braze
import com.braze.BrazeActivityLifecycleCallbackListener
import com.braze.configuration.BrazeConfig
import com.braze.support.BrazeLogger
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.integrations.ApiKeyProvider
import com.movableink.integrations.MSPInitializer

const val LOG_TAG = "BrazeProvider"

class BrazeProvider : MSPInitializer {
    override fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(
            BrazeActivityLifecycleCallbackListener(
                sessionHandlingEnabled = true,
                registerInAppMessageManager = true,
            ),
        )
        // set up braze config
        application.registerActivityLifecycleCallbacks(BrazeActivityLifecycleCallbackListener())
        BrazeInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(application)

        BrazeInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(application)
        Braze.getInstance(application).logCustomEvent("Testing")

        setBrazeIdentifiers(application)

//        BrazeInAppMessageManager.getInstance().setCustomInAppMessageManagerListener(
//            BrazeListener(this),
//        )

        val apiKey = ApiKeyProvider.getBrazeApiKey(application)
        val customEndPoint = ApiKeyProvider.getBrazeCustomEndPoint(application)
        val senderId = ApiKeyProvider.getFireBaseSenderId(application)

        val brazeConfig =
            BrazeConfig
                .Builder()
                .setApiKey(apiKey)
                .setCustomEndpoint(customEndPoint)
                .setFirebaseCloudMessagingSenderIdKey(senderId)
                .setIsFirebaseCloudMessagingRegistrationEnabled(true)
                .setHandlePushDeepLinksAutomatically(true)
                .setPushDeepLinkBackStackActivityEnabled(false)
                .setDefaultNotificationChannelName(application.getString(R.string.braze_channel_name))
                .build()
        Braze.configure(application, brazeConfig)

        BrazeLogger.logLevel = Log.VERBOSE
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
            if (!task.isSuccessful) {
                Log.w(LOG_TAG, "Exception while registering FCM token with Braze.", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Braze.getInstance(application).registeredPushToken = token
        }
    }

    override fun onActivityResumed(activity: Activity) {
      /*  if (activity is AppCompatActivity) {
            BrazeInAppMessageManager.getInstance().registerInAppMessageManager(activity)
        }*/
    }

    override fun onActivityCreated(activity: Activity) {
    }

    override fun deinitialize() {
        Log.d("DefaultMSP", "Default MSP Initialized.")
    }

    private fun setBrazeIdentifiers(application: Application) {
        val userID = application.getString(R.string.testUSerId, Build.PRODUCT ?: "unknown")
        Braze.getInstance(application).changeUser(userID)
        Braze.getInstance(application).currentUser?.setFirstName(application.getString(R.string.testuserName))
        Braze.getInstance(application).currentUser?.setLastName(application.getString(R.string.testUserLastName))
    }
}
