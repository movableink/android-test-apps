package com.movableink.app

import android.app.Activity
import com.braze.models.inappmessage.IInAppMessage
import com.braze.ui.inappmessage.InAppMessageOperation
import com.braze.ui.inappmessage.listeners.IInAppMessageManagerListener
import com.movableink.inked.MIClient
import com.movableink.inked.inAppMessage.MovableInAppClient

private const val KEY_MI_LINK = "mi_link"
class BrazeListener(activity: Activity) : IInAppMessageManagerListener {
    private val activity = activity

    override fun beforeInAppMessageDisplayed(inAppMessage: IInAppMessage): InAppMessageOperation {
        if (inAppMessage.extras.containsKey(KEY_MI_LINK)) {
         /*  let the MovableInk SDK handle this.
            Log the impression to Braze, ask MIClient to show the message, and return .discard to
            notify the Braze SDK that we don't want it to show anything.*/
            val movableLink = inAppMessage.extras[KEY_MI_LINK] as String
            MIClient.showInAppBrowser(
                activity,
                movableLink,
                listener = object : MovableInAppClient.OnUrlLoadingListener {
                    override fun onButtonClicked(value: String) {
                        // log button clicks to braze
                        // inAppMessage.logButtonClick(value)
                    }
                },
            )
        }
        return InAppMessageOperation.DISCARD
    }
}
