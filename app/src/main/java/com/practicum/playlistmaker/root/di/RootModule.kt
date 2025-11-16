package com.practicum.playlistmaker.root.di

import com.practicum.playlistmaker.root.ui.RootViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rootModule = module {
    viewModel { RootViewModel(get()) }
}