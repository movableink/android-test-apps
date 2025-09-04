package com.movableink.integrations
import android.content.Context
import java.io.IOException
import java.util.Properties

object ApiKeyProvider {


    private var properties: Properties? = null

    private fun loadProperties(context: Context): Properties {
        if (properties == null) {
            properties = Properties()
            try {
                context.assets.open("secrets.properties").use { inputStream ->
                    properties!!.load(inputStream)
                }
            } catch (e: IOException) {
                properties = Properties()
            }
        }
        return properties!!
    }

    fun getAirShipKey(context: Context): String = loadProperties(context).getProperty("AIRSHIP_APP_KEY", "")
    fun getAirShipAppSecret(context: Context): String = loadProperties(context).getProperty("AIRSHIP_APP_SECRET", "")
    fun getAppsFlyerKey(context: Context): String = loadProperties(context).getProperty("APPSFLYER_API_KEY", "")
    fun getMoEngageKey(context: Context): String = loadProperties(context).getProperty("MOENGAGE_APP_KEY", "")
    fun getXtremePushAppKey(context: Context): String = loadProperties(context).getProperty("XTREMEPUSH_APP_KEY", "")
    fun getXtremePushSenderId(context: Context): String = loadProperties(context).getProperty("XTREMEPUSH_SENDER_ID", "")
    fun getXtremePushPublicKey(context: Context): String = loadProperties(context).getProperty("XTREMEPUSH_PUBLIC_KEY", "")
    fun getBrazeApiKey(context: Context): String = loadProperties(context).getProperty("BRAZE_API_KEY", "")
    fun getBrazeCustomEndPoint(context: Context): String = loadProperties(context).getProperty("BRAZE_CUSTOM_END_POINT", "")
    fun getFireBaseSenderId(context: Context): String = loadProperties(context).getProperty("FIRE_BASE_SENDER_ID", "")


}