package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if(darkTheme)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}