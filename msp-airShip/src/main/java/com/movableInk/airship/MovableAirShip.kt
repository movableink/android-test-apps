import android.content.Context
import androidx.core.content.ContextCompat
import com.movableink.airship.R
import com.urbanairship.AirshipConfigOptions
import com.urbanairship.Autopilot
import com.urbanairship.push.notifications.NotificationProvider

class MovableAirShip : Autopilot() {
    override fun createAirshipConfigOptions(context: Context): AirshipConfigOptions? {
        val builder = AirshipConfigOptions.newBuilder()

        // Set default credentials. Alternatively you can set production and development separately
        builder
            .setAppKey("SQgywH2xQmuAdg936oGSnQ")
            .setAppSecret("b1RTUPFNRpyNP8uMQoAAsQ")

        // Set site. Either SITE_US or SITE_EU
        builder.setSite(AirshipConfigOptions.SITE_US)

        // Set Common Notification config
        builder
            .setNotificationAccentColor(ContextCompat.getColor(context, android.R.color.holo_purple))
            .setNotificationIcon(android.R.drawable.stat_notify_chat)
            .setNotificationChannel(NotificationProvider.DEFAULT_NOTIFICATION_CHANNEL)

        // Allowlists. Use * to allow anything
        builder.setUrlAllowList(arrayOf("*"))

        // Other config
        return builder.build()
    }
}
