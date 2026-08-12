package com.movableink.app.messaging

/**
 * The messaging SDK that is allowed to display in-app messages.
 * Mutually exclusive — only one provider shows in-app messages at a time.
 * Push notifications are always enabled for all providers regardless of this selection.
 */
enum class MessagingProvider(val title: String) {
    SFMC("Salesforce Marketing Cloud"),
    MOENGAGE("MoEngage"),
    BRAZE("Braze"),
    ;

    companion object {
        val DEFAULT = SFMC

        /** Parse a stored name back to a provider, falling back to [DEFAULT]. */
        fun fromName(name: String?): MessagingProvider =
            entries.firstOrNull { it.name == name } ?: DEFAULT
    }
}
