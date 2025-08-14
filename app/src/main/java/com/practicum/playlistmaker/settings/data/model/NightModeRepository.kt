package com.practicum.playlistmaker.settings.data.model

interface NightModeRepository {
    fun setNightMode(isNight: Boolean)
    fun getNightMode(): Boolean
}