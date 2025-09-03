package com.movableInk.moengage.inapp

import com.moengage.inapp.listeners.OnClickActionListener
import com.moengage.inapp.model.ClickData

/**
 * @author Umang Chamaria
 * Date: 2022/02/03
 */
class ClickActionCallback : OnClickActionListener {
    override fun onClick(clickData: ClickData): Boolean {
//        logcat { " onClick() $clickData" }
        // return true if the application is handling else false
        return false
    }
}
