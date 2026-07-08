package com.movableink.app.settings

import android.content.Context
import android.content.SharedPreferences
import com.movableink.app.messaging.MessagingProvider
import com.movableink.app.messaging.MoEngageAccount

/**
 * Centralized typed access to the app's settings SharedPreferences.
 * Wraps the existing "settings_prefs" store; leaves the existing "mi_u" key intact.
 */
class SettingsRepository(private val prefs: SharedPreferences) {

    var selectedProvider: MessagingProvider
        get() = MessagingProvider.fromName(prefs.getString(KEY_PROVIDER, null))
        set(value) = prefs.edit().putString(KEY_PROVIDER, value.name).apply()

    var selectedAccount: MoEngageAccount
        get() = MoEngageAccount.fromName(prefs.getString(KEY_ACCOUNT, null))
        set(value) = prefs.edit().putString(KEY_ACCOUNT, value.name).apply()

    val miu: String?
        get() = prefs.getString(KEY_MIU, null)

    companion object {
        const val PREFS_NAME = "settings_prefs"
        const val KEY_MIU = "mi_u"
        const val KEY_PROVIDER = "in_app_message_provider"
        const val KEY_ACCOUNT = "moengage_account"

        /** Convenience factory using the app's shared settings prefs. */
        fun from(context: Context): SettingsRepository =
            SettingsRepository(context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE))
    }
}
