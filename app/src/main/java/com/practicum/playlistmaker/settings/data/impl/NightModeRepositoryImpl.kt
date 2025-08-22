package com.practicum.playlistmaker.settings.data.impl

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.settings.data.model.NightModeRepository
import com.practicum.playlistmaker.settings.data.model.SettingsRepository

class NightModeRepositoryImpl(
    val resources: Resources,
    private val settingsRepository: SettingsRepository
) : NightModeRepository {
    companion object {
        const val NIGHT_MODE_VALUE = "NIGHT_MODE"
    }

    override fun setNightMode(isNight: Boolean) {
        settingsRepository.setString(NIGHT_MODE_VALUE, isNight.toString())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun getNightMode(): Boolean {
        return settingsRepository.getString(
            NIGHT_MODE_VALUE,
            resources.configuration.isNightModeActive.toString()
        ).toBoolean()
    }
}