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
import com.movableink.inked.MIClient

private const val TAG = "MainActivity "

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        setContent {
            ShoppingCartApp()
        }
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
        fetchClickableLink()
    }
    private fun fetchClickableLink() {
        val context = this
        MIClient.checkPasteboardOnInstall { resolvedLink ->
            Log.d("ksk", "haha $resolvedLink")
            Toast.makeText(this, " Text From CP: $resolvedLink", Toast.LENGTH_LONG).show()
            try {
                resolvedLink?.let {
                    val uri = Uri.parse(it)
                    if (uri != null) {
                        // Check if your app can handle this URI
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        if (intent.resolveActivity(packageManager) != null) {
                            Log.d(TAG, "fetchClickableLink: hahahah yeah {${intent.action}}")
                            startActivity(intent)
                        } else {
                            Log.d(TAG, "fetchClickableLink: hahahah Not")
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
