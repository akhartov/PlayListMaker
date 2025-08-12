package com.practicum.playlistmaker.settings.creator

import android.app.Application
import com.practicum.playlistmaker.settings.data.impl.ChangeApplicationNightModeUseCaseImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.data.impl.NightModeRepositoryImpl
import com.practicum.playlistmaker.settings.data.model.SettingsRepository
import com.practicum.playlistmaker.settings.data.model.ChangeApplicationNightModeUseCase
import com.practicum.playlistmaker.settings.data.model.NightModeRepository
import com.practicum.playlistmaker.settings.domain.impl.NightModeInteractorImpl
import com.practicum.playlistmaker.settings.domain.model.NightModeInteractor

object SettingsCreator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        SettingsCreator.application = application
    }

    private fun getSettingsManager(): SettingsRepository {
        return SettingsRepositoryImpl(application.applicationContext)
    }

    private fun getNightModeRepository(): NightModeRepository {
        return NightModeRepositoryImpl(application.applicationContext, getSettingsManager())
    }

    private fun getChangeApplicationNightModeUseCase(): ChangeApplicationNightModeUseCase {
        return ChangeApplicationNightModeUseCaseImpl()
    }

    fun getNightModeInteractor(): NightModeInteractor {
        return NightModeInteractorImpl(getNightModeRepository(), getChangeApplicationNightModeUseCase())
    }
}