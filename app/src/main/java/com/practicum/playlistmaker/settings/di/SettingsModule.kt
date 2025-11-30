package com.practicum.playlistmaker.settings.di

import android.content.Context
import com.practicum.playlistmaker.settings.data.impl.ChangeApplicationNightModeUseCaseImpl
import com.practicum.playlistmaker.settings.data.impl.NightModeRepositoryImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.data.model.ChangeApplicationNightModeUseCase
import com.practicum.playlistmaker.settings.data.model.NightModeRepository
import com.practicum.playlistmaker.settings.data.model.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.NightModeInteractorImpl
import com.practicum.playlistmaker.settings.domain.model.NightModeInteractor
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import com.practicum.playlistmaker.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.ExternalNavigator
import com.practicum.playlistmaker.data.SharingInteractorImpl
import com.practicum.playlistmaker.domain.SharingInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single {
        androidContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
    }

    factory<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }
    single<NightModeRepository> { NightModeRepositoryImpl(androidContext().resources, get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single<SharingInteractor> { SharingInteractorImpl(androidContext().resources, get()) }
    single<NightModeInteractor> { NightModeInteractorImpl(get(), get()) }
    factory<ChangeApplicationNightModeUseCase> { ChangeApplicationNightModeUseCaseImpl() }
}

val settingsViewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}