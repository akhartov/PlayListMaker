package com.practicum.playlistmaker.di

import androidx.room.Room
import com.practicum.playlistmaker.data.db.AppDatabase
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
}