package com.practicum.playlistmaker.settings.data.model

interface SettingsRepository {
    fun getString(key: String, defaultValue: String = ""): String
    fun setString(key: String, value: String)

    fun subscribe(listener: (String) -> Unit)

    fun cleanup()
}