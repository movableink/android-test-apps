package com.movableink.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.movableink.inked.MIClient

private const val TAG = "MainActivity "

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        getFCMToken()
        checkIntentExtras()
        setContent {
            ShoppingCartApp()
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

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "FCM Token: $token")
        })
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "askNotificationPermission: granted")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }


    private fun fetchClickableLink() {
        MIClient.checkPasteboardOnInstall { resolvedLink ->
            try {
                resolvedLink?.let {
                    val uri = Uri.parse(it)
                    if (uri != null) {
                        // Check if your app can handle this URI
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Log.d(TAG, "Cannot open the link")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "fetchClickableLink: Error :${e.message}")
            }
        }
    }
}
