package com.movableink.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.inked.MIClient
import com.salesforce.marketingcloud.events.EventManager
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // User just granted notification permission — notify SFMC to enable push
                enableSFMCPush()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        getFCMToken()
        fetchClickableLink()
        checkIntentExtras()
        setContent {
            ShoppingCartApp()
        }

        EventManager.customEvent("display_message", mapOf())?.track()
    }

    private fun enableSFMCPush() {
        SFMCSdk.requestSdk { sdk ->
            sdk.mp {
                it.pushMessageManager.enablePush()
            }
        }
    }

    private fun checkIntentExtras() {
        intent?.extras?.let { bundle ->
            for (key in bundle.keySet()) {
                bundle.getString(key)?.let { value ->
                    Log.d(TAG, "Intent Extra - Key: $key, Value: $value")
                }
            }
            MIClient.handlePushNotificationOpened(bundle)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        checkIntentExtras()
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "FCM Token: $token")

            val miu = getSharedPreferences("settings_prefs", MODE_PRIVATE)
                .getString("mi_u", null)
            SFMCSdk.requestSdk { sdk ->
                if (!miu.isNullOrEmpty()) {
                    sdk.identity.setProfileId(miu)
                }
//                sdk.mp {
//                    it.pushMessageManager.setPushToken(token)
//                }
            }
        })
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "askNotificationPermission: granted")
                enableSFMCPush()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Could show rationale UI here if needed
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Below Android 13, no runtime permission needed — enable push directly
            enableSFMCPush()
        }
    }

    private fun fetchClickableLink() {
        MIClient.checkPasteboardOnInstall { resolvedLink ->
            try {
                resolvedLink?.let {
                    val uri = Uri.parse(it)
                    if (uri != null) {
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Cannot open the link", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "fetchClickableLink: Error :${e.message}")
            }
        }
    }
}
