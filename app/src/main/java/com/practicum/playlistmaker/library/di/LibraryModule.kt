package com.practicum.playlistmaker.library.di

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.library.data.db.FavouritesRepositoryImpl
import com.practicum.playlistmaker.library.domain.FavouritesRepository
import com.practicum.playlistmaker.library.domain.FavouritesInteractor
import com.practicum.playlistmaker.library.domain.FavouritesInteractorImpl
import com.practicum.playlistmaker.library.ui.FavouritesTracksViewModel
import com.practicum.playlistmaker.library.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryViewModelModule = module {
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        FavouritesTracksViewModel(get())
    }

    factory {
        TrackMapper()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    single<FavouritesInteractor> { FavouritesInteractorImpl(get()) }
}