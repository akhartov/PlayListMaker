package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.ui.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



val playerViewModelModule = module {
    viewModel { (track: Track?) ->
        PlayerViewModel(track, get(), get())
    }
}