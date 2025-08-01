package com.practicum.playlistmaker.data.managers

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.managers.SettingsManager

class SettingsManagerImpl(context: Context) : SettingsManager {
    companion object {
        private const val SECTION_NAME = "SETTINGS"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SECTION_NAME, Context.MODE_PRIVATE)

    private val listeners = mutableSetOf<(String) -> Unit>()

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            listeners.forEach {
                if (key != null)
                    it.invoke(key)
            }
        }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun subscribe(listener: (String) -> Unit) {
        listeners.add(listener)
    }

    override fun cleanup() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        listeners.clear()
    }
}