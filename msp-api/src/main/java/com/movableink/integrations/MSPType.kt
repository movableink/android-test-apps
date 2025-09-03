package com.movableink.integrations

import androidx.annotation.DrawableRes

enum class MSPType(
    val displayName: String,
    @DrawableRes val iconRes: Int,
) {
    BRAZE("Braze", R.drawable.baseline_control_point_24),
    MOENGAGE("MoEngage", R.drawable.baseline_control_point_24),
    APPSFLYER("AppsFlyer", R.drawable.baseline_control_point_24),
    AIRSHIP("Airship", R.drawable.baseline_control_point_24),
    XTREMEPUSH("Xtremepush", R.drawable.baseline_control_point_24),
    NONE("None", R.drawable.baseline_control_point_24),
}
