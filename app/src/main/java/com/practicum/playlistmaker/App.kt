package com.practicum.playlistmaker

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.main.di.mainModule
import com.practicum.playlistmaker.main.di.mainViewModelModule
import com.practicum.playlistmaker.player.di.playerModule
import com.practicum.playlistmaker.player.di.playerViewModelModule
import com.practicum.playlistmaker.search.di.searchModule
import com.practicum.playlistmaker.search.di.searchViewModelModule
import com.practicum.playlistmaker.settings.di.settingsModule
import com.practicum.playlistmaker.settings.di.settingsViewModelModule
import com.practicum.playlistmaker.settings.domain.model.NightModeInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                mainModule, mainViewModelModule,
                searchModule, searchViewModelModule,
                playerModule, playerViewModelModule,
                settingsModule, settingsViewModelModule,
            )
        }

        (getKoin().get() as NightModeInteractor).restoreNightMode()
    }
}