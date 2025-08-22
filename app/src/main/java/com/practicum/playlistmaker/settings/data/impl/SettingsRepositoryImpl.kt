package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.data.model.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

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