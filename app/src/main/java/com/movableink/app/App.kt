package com.movableink.app

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.movableink.app.messaging.MessagingProvider
import com.movableink.app.messaging.MoEngageClient
import com.movableink.app.settings.SettingsRepository
import com.movableink.inked.MIClient
import com.movableink.inked.inAppMessage.MovableInAppClient
import com.salesforce.marketingcloud.MarketingCloudConfig
import com.salesforce.marketingcloud.inappmessaging.models.InAppMessage
import com.salesforce.marketingcloud.inappmessagingfeature.InAppMessageCloseAction
import com.salesforce.marketingcloud.inappmessagingfeature.InAppMessageManager
import com.salesforce.marketingcloud.inappmessagingfeature.config.InAppMessagingFeatureConfig
import com.salesforce.marketingcloud.pushfeature.config.PushFeatureConfig
import com.salesforce.marketingcloud.pushfeature.notifications.NotificationCustomizationOptions
import com.salesforce.marketingcloud.pushfeature.notifications.NotificationManager
import com.salesforce.marketingcloud.pushmodels.NotificationMessage
import com.salesforce.marketingcloud.sfmcsdk.InitializationStatus
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogLevel
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random
import androidx.core.net.toUri
import com.salesforce.marketingcloud.inappmessagingfeature.InAppMessagingFeature

private const val LOG_TAG: String = "Application"
private const val PREFS_NAME = "settings_prefs"
private const val KEY_MIU = "mi_u"

internal fun openDirectUrl(type: NotificationMessage.Type, url: String?): String? =
    url?.takeIf { type == NotificationMessage.Type.OPEN_DIRECT && it.isNotBlank() }

class App : Application() {
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) { currentActivity = activity }
            override fun onActivityPaused(activity: Activity) { currentActivity = null }
            override fun onActivityCreated(activity: Activity, savedInstanceState: android.os.Bundle?) = Unit
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: android.os.Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
        ensureMIU()
        MIClient.start()
        MIClient.registerDeeplinkDomains(
            listOf("afra.io"),
        )
        FirebaseApp.initializeApp(this)
        setUpMoEngage()
        setUpSalesForce()
    }

    private fun ensureMIU() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        if (prefs.getString(KEY_MIU, null).isNullOrEmpty()) {
            val generated = java.util.UUID.randomUUID().toString()
            prefs.edit().putString(KEY_MIU, generated).apply()
            Log.d(LOG_TAG, "Generated new MIU: $generated")
        }
    }

    private fun miu(): String? =
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_MIU, null)

    @Suppress("ktlint:standard:property-naming")
    private fun setUpSalesForce() {
        val mc_access_token = getString(R.string.accessToken)
        val mc_application_id = getString(R.string.mc_appId)
        val marketing_cloud_url = getString(R.string.marketing_cloud_url)
        val senderId = getString(R.string.fcm_sender_id)
        val mid = getString(R.string.mc_mid)

        Log.d(LOG_TAG, "SFMC: starting configuration")

        SFMCSdk.setLogging(LogLevel.DEBUG, LogListener.AndroidLogger())
        val notificationCustomizationOptions =
            NotificationCustomizationOptions.create(
                android.R.drawable.stat_notify_chat,
                { context, message ->
                    val intent = openDirectUrl(message.type, message.url)
                        ?.let { Intent(Intent.ACTION_VIEW, it.toUri()) }
                        ?: Intent(context, MainActivity::class.java)

                    PendingIntent.getActivity(
                        context,
                        Random().nextInt(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    )
                },
                { context, _ ->
                    NotificationManager.createDefaultNotificationChannel(context)
                },
            )

        val config = SFMCSdkModuleConfig.build {
            engagementModuleConfig =
                MarketingCloudConfig.Builder().apply {
                    setApplicationId(mc_application_id)
                    setAccessToken(mc_access_token)
                    setMarketingCloudServerUrl(marketing_cloud_url)
                    setMid(mid)
                    setAnalyticsEnabled(true)
                }.build(applicationContext)

            pushFeatureModuleConfig =
                PushFeatureConfig.builder()
                    .setSenderId(senderId)
                    .setNotificationCustomizationOptions(notificationCustomizationOptions)
                    .setUrlHandler { context, url, _ ->
                        PendingIntent.getActivity(
                            context,
                            Random().nextInt(),
                            Intent(Intent.ACTION_VIEW, url.toUri()),
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                        )
                    }.build()

            inAppMessagingFeatureModuleConfig = InAppMessagingFeatureConfig.builder().apply {
                setEventListener(inAppMessageEventListener())
            }.build()
        }

        SFMCSdk.configure(
            applicationContext,
            config,
            { initStatus ->
                when (initStatus.status) {
                    InitializationStatus.SUCCESS -> {
                        Log.d(LOG_TAG, "SFMC init: SUCCESS")
                        miu()?.let { miu ->
                            SFMCSdk.requestSdk { sdk ->
                                sdk.identity.edit { profileId = miu }
                                Log.d(LOG_TAG, "SFMC profile ID set: $miu")
                            }
                        }
                        MIClient.setMIU(miu() ?: "")

                        InAppMessagingFeature.requestSdk { feature ->
                            feature.getInAppMessageManager().setInAppMessageListener(inAppMessageEventListener())
                        }
                    }
                    InitializationStatus.FAILURE -> {
                        Log.e(LOG_TAG, "SFMC init: FAILED (status=${initStatus.status})")
                    }
                }
            },
        )
    }

    private fun inAppMessageEventListener() = object : InAppMessageManager.EventListener {
        override fun shouldShowMessage(message: InAppMessage): Boolean {
            Log.d(LOG_TAG, "SFMC - shouldShowMessage: $message")
            // Mutual exclusivity: suppress SFMC in-app unless SFMC is the selected provider.
            if (SettingsRepository.from(this@App).selectedProvider != MessagingProvider.SFMC) {
                Log.d(LOG_TAG, "SFMC - suppressing in app - not selected provider")
                return false
            }

            val text = message.title?.text
            if (text != null && text.startsWith("mi_link:")) {
                val miLink = text.drop("mi_link:".length)
                val activity = currentActivity ?: run {
                    Log.w(LOG_TAG, "SFMC: no resumed activity for MI link")
                    return true
                }
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        Log.d(LOG_TAG, "SFMC: opening MI link in ${activity.javaClass.simpleName}")
                        MIClient.showInAppBrowser(
                            activity,
                            miLink,
                            listener = object : MovableInAppClient.OnUrlLoadingListener {
                                override fun onButtonClicked(buttonID: String) {
                                    // User interacted with a link that has a buttonID
                                }
                            },
                        )
                        Log.d(LOG_TAG, "SFMC: MI browser launch requested")
                    } catch (error: Exception) {
                        Log.e(LOG_TAG, "SFMC: failed to open MI link", error)
                    }
                }

                return false
            }

            return true
        }

        override fun didShowMessage(message: InAppMessage) {
            Log.d(LOG_TAG, "SFMC IAM shown: ${message.id}")
        }

        override fun didCloseMessage(message: InAppMessage, action: InAppMessageCloseAction) {
            Log.d(LOG_TAG, "SFMC IAM closed: ${message.id}")
        }
    }

    private fun setUpMoEngage() {
        val repo = SettingsRepository.from(this)
        val account = repo.selectedAccount
        MoEngageClient.initialize(this, account)
        miu()?.let { storedMiu ->
            if (storedMiu.isNotEmpty()) {
                MoEngageClient.identify(this, storedMiu)
                Log.d(LOG_TAG, "MoEngage identify: $storedMiu")
            }
        }
    }
}
