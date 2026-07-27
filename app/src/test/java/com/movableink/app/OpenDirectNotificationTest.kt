package com.movableink.app

import com.salesforce.marketingcloud.notifications.NotificationMessage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class OpenDirectNotificationTest {
    @Test
    fun openDirectUrl_returnsUrlForOpenDirectMessage() {
        assertEquals(
            "https://www.movable-ink-7158.com/p/cpm/example",
            openDirectUrl(
                NotificationMessage.Type.OPEN_DIRECT,
                "https://www.movable-ink-7158.com/p/cpm/example",
            ),
        )
    }

    @Test
    fun openDirectUrl_ignoresOtherNotificationTypes() {
        assertNull(
            openDirectUrl(
                NotificationMessage.Type.CLOUD_PAGE,
                "https://www.movable-ink-7158.com/p/cpm/example",
            ),
        )
    }
}
