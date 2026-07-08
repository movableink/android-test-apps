package com.movableink.app.messaging

import com.moengage.core.DataCenter

/**
 * A selectable MoEngage workspace/account.
 *
 * NOTE: These App IDs are reused from the iOS app. MoEngage App IDs can be
 * platform-specific (a separate Android workspace), so Android push may require
 * Android-workspace App IDs. This is a testing caveat, not a code issue.
 */
enum class MoEngageAccount(
    val appId: String,
    val dataCenter: DataCenter,
    val title: String,
) {
    PARTNER_SANDBOX("CF6VET3G5MRFCA7CML6D37ND", DataCenter.DATA_CENTER_1, "Partner Sandbox"),
    DEMO_ECOMMERCE("TAQGW6TG2CFSQMH5P0NHXBIH", DataCenter.DATA_CENTER_4, "Demo Account - Ecommerce"),
    ;

    companion object {
        val DEFAULT = PARTNER_SANDBOX

        /** Parse a stored name back to an account, falling back to [DEFAULT]. */
        fun fromName(name: String?): MoEngageAccount =
            entries.firstOrNull { it.name == name } ?: DEFAULT
    }
}
