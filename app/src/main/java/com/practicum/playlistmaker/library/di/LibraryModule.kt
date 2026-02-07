package com.practicum.playlistmaker.library.di

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.library.data.FavouritesRepositoryImpl
import com.practicum.playlistmaker.library.domain.FavouritesRepository
import com.practicum.playlistmaker.library.domain.FavouritesInteractor
import com.practicum.playlistmaker.library.domain.FavouritesInteractorImpl
import com.practicum.playlistmaker.library.presentation.LibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    viewModel {
        LibraryViewModel(get(), get())
    }

    factory {
        TrackMapper()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    single<FavouritesInteractor> { FavouritesInteractorImpl(get()) }
}