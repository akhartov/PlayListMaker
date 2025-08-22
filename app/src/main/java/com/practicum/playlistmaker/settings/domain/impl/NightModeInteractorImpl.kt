package com.practicum.playlistmaker.settings.domain.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.settings.data.model.ChangeApplicationNightModeUseCase
import com.practicum.playlistmaker.settings.data.model.NightModeRepository
import com.practicum.playlistmaker.settings.domain.model.NightModeInteractor

class NightModeInteractorImpl(
    private val nightModeRepository: NightModeRepository,
    private val changeApplicationNightModeUseCase: ChangeApplicationNightModeUseCase
) : NightModeInteractor {
    override fun setNightMode(isNight: Boolean) {
        nightModeRepository.setNightMode(isNight)
        changeApplicationNightModeUseCase.setNightMode(isNight)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun getNightMode(): Boolean {
        return nightModeRepository.getNightMode()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun restoreNightMode() {
        changeApplicationNightModeUseCase.setNightMode(nightModeRepository.getNightMode())
    }
}