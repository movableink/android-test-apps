package com.movableink.app

import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.util.Log
import android.view.Display.DEFAULT_DISPLAY
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.FirebaseApp
import com.movableink.inked.MIClient
import com.movableink.integrations.MSPManager

private const val LOG_TAG: String = "Application"
// val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "msp_settings")

class App : Application() {
    @Suppress("ktlint:standard:no-consecutive-comments")
    override fun onCreate() {
        super.onCreate()
        MIClient.start()
        MIClient.registerDeeplinkDomains(
            listOf("afra.io"),
        )
        MIClient.appInstallEventEnabled(true)
        FirebaseApp.initializeApp(this)
        @Suppress("ktlint:standard:property-naming")
        val SELECTED_MSP_KEY = stringPreferencesKey("selected_msp")

    /*    val selectedMsp =
            runBlocking {
                dataStore.data
                    .map { prefs -> prefs[SELECTED_MSP_KEY] ?: "" }
                    .firstOrNull()
            }*/

     /*   if (selectedMsp.isNullOrEmpty().not()) {
            selectedMsp?.let {
                val msp = MSPType.valueOf(selectedMsp)
                MSPManager.initialize(this)
            }
        }*/
        MSPManager.initialize(this)
        Log.d("MSPManager", "onCreate: ")
    }

    private fun Context.displayContext(): Context {
        val manager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val display = manager.getDisplay(DEFAULT_DISPLAY)
        return createDisplayContext(display)
    }
}
