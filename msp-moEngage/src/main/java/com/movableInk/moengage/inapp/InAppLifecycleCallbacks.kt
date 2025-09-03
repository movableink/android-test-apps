package com.movableInk.moengage.inapp

import com.moengage.inapp.listeners.InAppLifeCycleListener
import com.moengage.inapp.model.InAppData

class InAppLifecycleCallbacks : InAppLifeCycleListener {
    override fun onDismiss(inAppData: InAppData) {
//        logcat { " onDismiss() Data: $inAppData" }
    }

    override fun onShown(inAppData: InAppData) {
//        logcat { " onShown() Data: $inAppData" }
    }
}
