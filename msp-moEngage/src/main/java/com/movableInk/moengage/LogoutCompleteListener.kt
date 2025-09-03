package com.movableInk.moengage

import com.moengage.core.listeners.OnLogoutCompleteListener
import com.moengage.core.model.LogoutData

/**
 * @author Umang Chamaria
 * Date: 2022/02/15
 */
class LogoutCompleteListener : OnLogoutCompleteListener {
    override fun logoutComplete(data: LogoutData) {
        // logcat { "logoutComplete() $data" }
    }
}
