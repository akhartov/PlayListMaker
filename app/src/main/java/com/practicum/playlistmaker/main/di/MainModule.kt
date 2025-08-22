package com.practicum.playlistmaker.main.di

import com.practicum.playlistmaker.main.data.ScreenNavigatorImpl
import com.practicum.playlistmaker.main.domain.ScreenNavigator
import com.practicum.playlistmaker.main.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<ScreenNavigator> { ScreenNavigatorImpl(androidContext()) }
}

val mainViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}