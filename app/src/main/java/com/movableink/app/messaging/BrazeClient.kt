package com.movableink.app.messaging

import android.app.Application
import android.content.Context
import com.braze.Braze
import com.braze.BrazeActivityLifecycleCallbackListener
import com.braze.configuration.BrazeConfig
import com.braze.push.BrazeFirebaseMessagingService
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.braze.ui.inappmessage.InAppMessageOperation
import com.braze.ui.inappmessage.listeners.DefaultInAppMessageManagerListener
import com.google.firebase.messaging.RemoteMessage
import com.movableink.app.R
import com.movableink.app.settings.SettingsRepository

/** Braze integration for the app-owned FCM service and IAM provider selector. */
object BrazeClient {

    fun initialize(app: Application) {
        Braze.configure(
            app,
            BrazeConfig.Builder()
                .setApiKey(app.getString(R.string.braze_api_key))
                .setCustomEndpoint(app.getString(R.string.braze_sdk_endpoint))
                .setIsFirebaseCloudMessagingRegistrationEnabled(false)
                .build(),
        )
        BrazeInAppMessageManager.getInstance().setCustomInAppMessageManagerListener(
            object : DefaultInAppMessageManagerListener() {
                override fun beforeInAppMessageDisplayed(inAppMessage: com.braze.models.inappmessage.IInAppMessage): InAppMessageOperation =
                    if (SettingsRepository.from(app).selectedProvider == MessagingProvider.BRAZE) {
                        InAppMessageOperation.DISPLAY_NOW
                    } else {
                        InAppMessageOperation.DISCARD
                    }
            },
        )
        app.registerActivityLifecycleCallbacks(
            BrazeActivityLifecycleCallbackListener(
                sessionHandlingEnabled = true,
                registerInAppMessageManager = true,
            ),
        )
    }

    fun identify(context: Context, miu: String) {
        if (miu.isNotBlank()) {
            Braze.getInstance(context).changeUser(miu)
        }
    }

    fun passPushToken(context: Context, token: String) {
        Braze.getInstance(context).registeredPushToken = token
    }

    fun handlePushPayload(context: Context, message: RemoteMessage): Boolean =
        BrazeFirebaseMessagingService.handleBrazeRemoteMessage(context, message)
}
