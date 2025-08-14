package com.practicum.playlistmaker.settings.domain.model

interface NightModeInteractor {
    fun setNightMode(isNight: Boolean)
    fun getNightMode(): Boolean
    fun restoreNightMode()
}