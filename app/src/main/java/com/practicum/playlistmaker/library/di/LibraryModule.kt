package com.practicum.playlistmaker.library.di

import com.practicum.playlistmaker.library.history.FavouritesTracksViewModel
import com.practicum.playlistmaker.library.playlists.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryViewModelModule = module {
    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        FavouritesTracksViewModel()
    }
}