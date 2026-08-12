package com.movableink.app.settings

import android.content.SharedPreferences
import com.movableink.app.messaging.MessagingProvider
import com.movableink.app.messaging.MoEngageAccount
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsRepositoryTest {

    /** Minimal in-memory SharedPreferences fake for JVM unit tests. */
    private class FakePrefs : SharedPreferences {
        val map = mutableMapOf<String, String?>()

        private inner class FakeEditor : SharedPreferences.Editor {
            override fun putString(key: String, value: String?): SharedPreferences.Editor {
                map[key] = value; return this
            }
            override fun apply() = Unit
            override fun commit() = true
            override fun clear(): SharedPreferences.Editor { map.clear(); return this }
            override fun remove(key: String): SharedPreferences.Editor { map.remove(key); return this }
            override fun putInt(k: String, v: Int) = this
            override fun putLong(k: String, v: Long) = this
            override fun putFloat(k: String, v: Float) = this
            override fun putBoolean(k: String, v: Boolean) = this
            override fun putStringSet(k: String, v: MutableSet<String>?) = this
        }

        override fun getString(key: String, defValue: String?): String? = map[key] ?: defValue
        override fun edit(): SharedPreferences.Editor = FakeEditor()
        override fun contains(key: String): Boolean = map.containsKey(key)
        override fun getAll(): MutableMap<String, *> = map
        override fun getInt(k: String, d: Int) = d
        override fun getLong(k: String, d: Long) = d
        override fun getFloat(k: String, d: Float) = d
        override fun getBoolean(k: String, d: Boolean) = d
        override fun getStringSet(k: String, d: MutableSet<String>?) = d
        override fun registerOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) = Unit
        override fun unregisterOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) = Unit
    }

    @Test
    fun `selectedProvider defaults to SFMC when unset`() {
        val repo = SettingsRepository(FakePrefs())
        assertEquals(MessagingProvider.SFMC, repo.selectedProvider)
    }

    @Test
    fun `selectedProvider round-trips through storage`() {
        val repo = SettingsRepository(FakePrefs())
        repo.selectedProvider = MessagingProvider.MOENGAGE
        assertEquals(MessagingProvider.MOENGAGE, repo.selectedProvider)
    }

    @Test
    fun `selectedProvider persists Braze`() {
        val repo = SettingsRepository(FakePrefs())

        repo.selectedProvider = MessagingProvider.BRAZE

        assertEquals(MessagingProvider.BRAZE, repo.selectedProvider)
    }

    @Test
    fun `selectedAccount defaults to partner sandbox when unset`() {
        val repo = SettingsRepository(FakePrefs())
        assertEquals(MoEngageAccount.PARTNER_SANDBOX, repo.selectedAccount)
    }

    @Test
    fun `selectedAccount round-trips through storage`() {
        val repo = SettingsRepository(FakePrefs())
        repo.selectedAccount = MoEngageAccount.DEMO_ECOMMERCE
        assertEquals(MoEngageAccount.DEMO_ECOMMERCE, repo.selectedAccount)
    }
}
