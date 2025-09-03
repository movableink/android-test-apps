package com.movableink.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MIFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle notification when app is in foreground
        remoteMessage.notification?.let { notification ->
            val title = notification.title
            val body = notification.body

            // Handle notification data payload
            val data = remoteMessage.data

            // Create and show notification
            createNotification(title, body, data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
    private fun createNotification(
        title: String?,
        body: String?,
        data: Map<String, String>,
    ) {
        val channelId = "default_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT,
                )
            notificationManager.createNotificationChannel(channel)
        }

        // Build notification
        val builder =
            NotificationCompat
                .Builder(this, channelId)
        .setSmallIcon(android.R.drawable.btn_default_small)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)

        // Show notification
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
