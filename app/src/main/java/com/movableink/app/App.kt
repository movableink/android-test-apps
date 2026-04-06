package com.movableink.app

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseApp
import com.movableink.inked.MIClient
import com.movableink.inked.inAppMessage.MovableInAppClient
import com.salesforce.marketingcloud.MCLogListener
import com.salesforce.marketingcloud.MarketingCloudConfig
import com.salesforce.marketingcloud.MarketingCloudSdk
import com.salesforce.marketingcloud.events.EventManager
import com.salesforce.marketingcloud.messages.iam.InAppMessage
import com.salesforce.marketingcloud.messages.iam.InAppMessageManager
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions
import com.salesforce.marketingcloud.sfmcsdk.InitializationStatus
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogLevel
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random

private const val LOG_TAG: String = "Application"
private const val PREFS_NAME = "settings_prefs"
private const val KEY_MIU = "mi_u"

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
        MarketingCloudSdk.setLogLevel(MCLogListener.VERBOSE)
        MarketingCloudSdk.setLogListener(MCLogListener.AndroidLogListener())

        val config = SFMCSdkModuleConfig.build {
            pushModuleConfig =
                MarketingCloudConfig
                    .builder()
                    .apply {
                        setApplicationId(mc_application_id)
                        setAccessToken(mc_access_token)
                        setMarketingCloudServerUrl(marketing_cloud_url)
                        setSenderId(senderId)
                        setMid(mid)
                        setAnalyticsEnabled(true)
                        setNotificationCustomizationOptions(
                            NotificationCustomizationOptions.create(android.R.drawable.stat_notify_chat),
                        )
                        setUrlHandler { context, url, _ ->
                            PendingIntent.getActivity(
                                context,
                                Random().nextInt(),
                                Intent(Intent.ACTION_VIEW, Uri.parse(url)),
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                            )
                        }
                    }.build(applicationContext)
        }

        SFMCSdk.configure(
            applicationContext as Application,
            config,
            { initStatus ->
                when (initStatus.status) {
                    InitializationStatus.SUCCESS -> {
                        Log.d(LOG_TAG, "SFMC init: SUCCESS")
                        miu()?.let { miu ->
                            SFMCSdk.requestSdk { sdk ->
                                sdk.identity.setProfileId(miu)
                                Log.d(LOG_TAG, "SFMC profile ID set: $miu")
                            }
                        }
                        MIClient.setMIU(miu() ?: "")
                    }
                    InitializationStatus.FAILURE -> {
                        Log.e(LOG_TAG, "SFMC init: FAILED (status=${initStatus.status})")
                    }
                }
            },
        )

        SFMCSdk.requestSdk { sdk ->
            sdk.mp {
                it.inAppMessageManager.setInAppMessageListener(object : InAppMessageManager.EventListener {
                    override fun shouldShowMessage(message: InAppMessage): Boolean {
                        val text = message.title?.text
                        if (text != null && text.startsWith("mi_link:")) {
                            val miLink = text.drop("mi_link:".length)
                            val activity = currentActivity ?: return@shouldShowMessage true
                            CoroutineScope(Dispatchers.Main).launch {
                                MIClient.showInAppBrowser(
                                    activity,
                                    miLink,
                                    listener = object : MovableInAppClient.OnUrlLoadingListener {
                                        override fun onButtonClicked(buttonID: String) {
                                            // User interacted with a link that has a buttonID
                                        }
                                    },
                                )
                            }
                            return false
                        }
                        return true
                    }

                    override fun didShowMessage(message: InAppMessage) {
                        Log.d(LOG_TAG, "IAM shown: ${message.id}")
                    }

                    override fun didCloseMessage(message: InAppMessage) {
                        Log.d(LOG_TAG, "IAM closed: ${message.id}")
                    }
                })
            }
        }
    }
}
