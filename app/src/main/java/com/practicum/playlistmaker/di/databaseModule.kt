package com.practicum.playlistmaker.di

import androidx.room.Room
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackDao
import com.practicum.playlistmaker.playlist.data.db.CoverDao
import com.practicum.playlistmaker.playlist.data.db.CoverTrackDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app.db"
        ).
        build()
    }

    single<FavouriteTrackDao> { get<AppDatabase>().favouriteTrackDao() }
    single<CoverTrackDao> { get<AppDatabase>().coverTrackDao() }
    single<CoverDao> { get<AppDatabase>().coverDao() }
}