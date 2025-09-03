package com.movableInk.moengage

import android.app.Activity
import android.app.Application
import android.util.Log
import com.moengage.core.DataCenter
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.PushKitConfig
import com.moengage.core.enableSdk
import com.moengage.inapp.MoEInAppHelper
import com.movableInk.moengage.inapp.ClickActionCallback
import com.movableInk.moengage.inapp.InAppLifecycleCallbacks
import com.movableInk.moengage.inapp.SelfHandledCallback
import com.movableink.integrations.MSPInitializer
import java.util.Date

class MoEngageProvider : MSPInitializer {
    override fun initialize(application: Application) {
        val moEngage =
            MoEngage
                .Builder(application, "TAQGW6TG2CFSQMH5P0NHXBIH", DataCenter.DATA_CENTER_4)
                .configureFcm(FcmConfig(true))
                .configurePushKit(PushKitConfig(true))
                .build()
        MoEngage.initialiseDefaultInstance(moEngage = moEngage)
        // register for application background listener
        MoECoreHelper.addAppBackgroundListener(ApplicationBackgroundListener())
        // register for logout complete listener
        MoECoreHelper.addLogoutCompleteListener(LogoutCompleteListener())
        setupInAppCallbacks()
        enableSdk(application)
    }

    private fun setupInAppCallbacks() {
        // callback for in-app campaign click
        MoEInAppHelper.getInstance().setClickActionListener(ClickActionCallback())
        // callback for in-app lifecycle - campaign shown/dismissed.
        MoEInAppHelper.getInstance().addInAppLifeCycleListener(InAppLifecycleCallbacks())
        // callback for self handled campaigns that are triggered based on events.
        MoEInAppHelper.getInstance().setSelfHandledListener(SelfHandledCallback())
    }

    override fun onActivityResumed(activity: Activity) {
        MoEInAppHelper.getInstance().showInApp(activity)
    }

    override fun onActivityCreated(activity: Activity) {
        MoECoreHelper.logoutUser(activity)
        MoEAnalyticsHelper.setUniqueId(activity, "Droid-1945-202222000")

           MoEAnalyticsHelper.setAlias(activity,"Droid-1945-20w2225")
     val properties = Properties()
     properties.addAttribute("attributeString", "string")
         .addAttribute("attributeInteger", 123)
         .addAttribute("attributeDate", Date())
         .addDateIso("attributeDateIso", "2022-02-10T21:12:00Z")
     MoEAnalyticsHelper.trackEvent(activity, "EVENT_Movable", properties)

     // user attribute tracking
     MoEAnalyticsHelper.setFirstName(activity, "EL")
     MoEAnalyticsHelper.setLastName(activity, "Quasimodo")
     MoEAnalyticsHelper.setBirthDate(activity, Date())
     MoEAnalyticsHelper.setEmailId(activity, "integrations@movableink.com")
    }

    override fun deinitialize() {
        Log.d("DefaultMSP", "Default MSP Initialized.")
    }
}
