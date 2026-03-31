package com.movableink.app

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import com.google.firebase.FirebaseApp
import com.movableink.inked.BuildConfig
import com.movableink.inked.MIClient
import com.salesforce.marketingcloud.MCLogListener
import com.salesforce.marketingcloud.MarketingCloudConfig
import com.salesforce.marketingcloud.MarketingCloudSdk
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogLevel
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogListener
import java.util.Random

private const val LOG_TAG: String = "Application"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MIClient.start()
        MIClient.registerDeeplinkDomains(
            listOf("afra.io"),
        )
        FirebaseApp.initializeApp(this)
        setUpSalesForce()
    }

    @Suppress("ktlint:standard:property-naming")
    private fun setUpSalesForce() {
        val mc_access_token = getString(R.string.accessToken)
        val mc_application_id = getString(R.string.mc_appId)
        val fcm_sender_id = getString(R.string.fcm_sender_id)
        val marketing_cloud_url = getString(R.string.marketing_cloud_url)
        if (BuildConfig.DEBUG) {
            SFMCSdk.setLogging(LogLevel.DEBUG, LogListener.AndroidLogger())
            MarketingCloudSdk.setLogLevel(MCLogListener.VERBOSE)
            MarketingCloudSdk.setLogListener(MCLogListener.AndroidLogListener())
        }

        SFMCSdk.configure(
            applicationContext as Application,
            SFMCSdkModuleConfig.build {
                pushModuleConfig =
                    MarketingCloudConfig
                        .builder()
                        .apply {
                            setApplicationId(mc_application_id)
                            setAccessToken(mc_access_token)
                            setSenderId(fcm_sender_id)
                            setMarketingCloudServerUrl(marketing_cloud_url)
                            setNotificationCustomizationOptions(
                                NotificationCustomizationOptions.create(android.R.drawable.stat_notify_chat),
                            ).setUrlHandler { context, url, _ ->
                                PendingIntent.getActivity(
                                    context,
                                    Random().nextInt(),
                                    Intent(Intent.ACTION_VIEW, Uri.parse(url)),
                                    PendingIntent.FLAG_UPDATE_CURRENT,
                                )
                            }
                        }.build(applicationContext)
            },
        ) { initStatus ->
        }

        SFMCSdk.requestSdk { sdk ->
            sdk.mp {
                it.pushMessageManager.enablePush()
            }
        }
    }
}
