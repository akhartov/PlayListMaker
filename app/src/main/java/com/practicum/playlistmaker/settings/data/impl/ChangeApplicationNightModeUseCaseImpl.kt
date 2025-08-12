package com.practicum.playlistmaker.settings.data.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.model.ChangeApplicationNightModeUseCase

class ChangeApplicationNightModeUseCaseImpl: ChangeApplicationNightModeUseCase {
    override fun setNightMode(isNight: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isNight)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}