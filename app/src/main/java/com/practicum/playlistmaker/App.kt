package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.di.databaseModule
import com.practicum.playlistmaker.favourites.di.favouritesModule
import com.practicum.playlistmaker.player.di.playerViewModelModule
import com.practicum.playlistmaker.playlist.di.playlistViewModelModule
import com.practicum.playlistmaker.root.di.rootModule
import com.practicum.playlistmaker.search.di.searchModule
import com.practicum.playlistmaker.search.di.searchViewModelModule
import com.practicum.playlistmaker.settings.di.settingsModule
import com.practicum.playlistmaker.settings.di.settingsViewModelModule
import com.practicum.playlistmaker.settings.domain.model.NightModeInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                rootModule, databaseModule,
                searchModule, searchViewModelModule,
                playerViewModelModule,
                settingsModule, settingsViewModelModule,
                favouritesModule,
                playlistViewModelModule
            )
        }

        (getKoin().get() as NightModeInteractor).restoreNightMode()
    }
}