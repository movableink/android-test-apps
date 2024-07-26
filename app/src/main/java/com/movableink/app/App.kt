package com.movableink.app

import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display.DEFAULT_DISPLAY
import com.appsflyer.AppsFlyerLib
import com.google.firebase.FirebaseApp
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
        MIClient.appInstallEventEnabled(true)
        FirebaseApp.initializeApp(this)

    }

    private fun Context.displayContext(): Context {
        val manager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val display = manager.getDisplay(DEFAULT_DISPLAY)
        return createDisplayContext(display)
    }
}
