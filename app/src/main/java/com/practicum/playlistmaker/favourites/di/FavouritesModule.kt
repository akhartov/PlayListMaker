package com.practicum.playlistmaker.favourites.di

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.favourites.data.FavouritesRepositoryImpl
import com.practicum.playlistmaker.favourites.domain.FavouritesRepository
import com.practicum.playlistmaker.favourites.domain.FavouritesInteractor
import com.practicum.playlistmaker.favourites.domain.FavouritesInteractorImpl
import com.practicum.playlistmaker.favourites.ui.FavouritesTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesModule = module {
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