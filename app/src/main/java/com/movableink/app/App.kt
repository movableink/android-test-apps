package com.movableink.app

import android.app.Application
import com.braze.Braze
import com.braze.BrazeActivityLifecycleCallbackListener
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.movableink.inked.MIClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MIClient.start()
        Braze.getInstance(applicationContext).logCustomEvent("Testing")
        BrazeInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(applicationContext)
        registerActivityLifecycleCallbacks(
            BrazeActivityLifecycleCallbackListener(
                true,
                true,
            ),
        )
    }
}
