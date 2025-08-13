package com.practicum.playlistmaker

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.main.creator.MainCreator
import com.practicum.playlistmaker.player.creator.PlayerCreator
import com.practicum.playlistmaker.search.creator.SearchCreator
import com.practicum.playlistmaker.settings.creator.SettingsCreator
import com.practicum.playlistmaker.sharing.creator.SharingCreator

class App : Application() {
    private val nightModeInteractor by lazy { SettingsCreator.getNightModeInteractor() }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()
        MainCreator.initApplication(this)
        SearchCreator.initApplication(this)
        PlayerCreator.initApplication(this)
        SettingsCreator.initApplication(this)
        SharingCreator.initApplication(this)

        nightModeInteractor.restoreNightMode()
    }
}