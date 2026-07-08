package com.movableink.app.messaging

import android.app.Application
import android.content.Context
import android.util.Log
import com.moengage.core.MoEngage
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.inapp.MoEInAppHelper
import com.movableink.app.R

/**
 * Thin wrapper around the MoEngage SDK. Mirrors the iOS `MoEngageClient`.
 *
 * Push registration is owned by the app's single FirebaseMessagingService, so we
 * disable MoEngage's own FCM registration and forward the token/payload ourselves.
 */
object MoEngageClient {

    private const val TAG = "MoEngageClient"

    /** Initialize the default MoEngage instance for the given account. Call in Application.onCreate(). */
    fun initialize(app: Application, account: MoEngageAccount) {
        Log.d(TAG, "Initializing MoEngage for account=${account.name} appId=${account.appId} dc=${account.dataCenter}")
        val moEngage = MoEngage.Builder(app, account.appId, account.dataCenter)
            .configureNotificationMetaData(
                NotificationConfig(
                    // Small icon is MANDATORY — notifications will not display without it.
                    smallIcon = R.mipmap.ic_launcher,
                    largeIcon = R.mipmap.ic_launcher,
                ),
            )
            // The app owns the single FCM service and forwards the token via passPushToken().
            .configureFcm(FcmConfig(isRegistrationEnabled = false))
            .build()
        MoEngage.initialiseDefaultInstance(moEngage)
    }

    /** Associate the current MIU with the MoEngage user */
    fun identify(context: Context, miu: String) {
        if (miu.isBlank()) return
        MoEAnalyticsHelper.identifyUser(context, miu)
    }

    /** Forward the FCM token to MoEngage. */
    fun passPushToken(context: Context, token: String) {
        MoEFireBaseHelper.getInstance().passPushToken(context, token)
    }

    /** Forward a MoEngage push payload to the SDK for display. */
    fun passPushPayload(context: Context, payload: Map<String, String>) {
        MoEFireBaseHelper.getInstance().passPushPayload(context, payload)
    }

    /** Attempt to show a MoEngage in-app message. Only call when MoEngage is the selected provider. */
    fun showInApp(context: Context) {
        MoEInAppHelper.getInstance().showInApp(context)
    }
}
