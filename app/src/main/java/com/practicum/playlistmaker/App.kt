package com.practicum.playlistmaker

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.Creator

class App : Application() {
    var darkTheme = false
    private val settings by lazy { Creator.getSettingsInteractor() }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        switchTheme(
            settings.getNightTheme(applicationContext.resources.configuration.isNightModeActive)
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}