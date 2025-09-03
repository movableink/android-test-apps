package com.movableInk.moengage

import android.content.Context
import com.moengage.core.listeners.AppBackgroundListener
import com.moengage.core.model.AppBackgroundData

/**
 * @author Umang Chamaria
 * Date: 2022/02/15
 */
class ApplicationBackgroundListener : AppBackgroundListener {
    override fun onAppBackground(
        context: Context,
        data: AppBackgroundData,
    ) {
        // logcat { "onAppBackground() $data" }
    }
}
