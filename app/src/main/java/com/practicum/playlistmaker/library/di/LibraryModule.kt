package com.practicum.playlistmaker.library.di

import androidx.room.Room
import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.data.db.FavouritesRepositoryImpl
import com.practicum.playlistmaker.library.db.FavouritesRepository
import com.practicum.playlistmaker.library.domain.FavouritesInteractor
import com.practicum.playlistmaker.library.domain.FavouritesInteractorImpl
import com.practicum.playlistmaker.library.ui.FavouritesTracksViewModel
import com.practicum.playlistmaker.library.ui.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryViewModelModule = module {
    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        FavouritesTracksViewModel(get())
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "track_history.db"
        ).build()
    }

    factory {
        TrackMapper()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    single<FavouritesInteractor> { FavouritesInteractorImpl(get()) }
}