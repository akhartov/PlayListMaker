package com.practicum.playlistmaker.domain.managers

interface SettingsManager {
    fun getString(key: String, defaultValue: String = ""): String
    fun setString(key: String, value: String)

    fun subscribe(listener: (String) -> Unit)

    fun cleanup()
}