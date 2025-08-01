package com.practicum.playlistmaker.domain.api

interface SettingsInteractor {
    companion object {
        const val NIGHT_MODE_VALUE = "NIGHT_MODE"
    }

    fun setNightTheme(isNight: Boolean)
    fun getNightTheme(defaultValue: Boolean): Boolean

    fun subscribe(listener: (String) -> Unit)
}