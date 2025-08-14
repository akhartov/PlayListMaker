package com.practicum.playlistmaker.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.library.ui.LibraryActivity
import com.practicum.playlistmaker.main.domain.ScreenNavigator
import com.practicum.playlistmaker.main.domain.ScreenType
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class ScreenNavigatorImpl(private val context: Context) : ScreenNavigator {

    private fun screenTypeToActivityClass(screenType: ScreenType): Class<out Activity> {
        return when (screenType) {
            ScreenType.Settings -> SettingsActivity::class.java
            ScreenType.Library -> LibraryActivity::class.java
            ScreenType.Search -> SearchActivity::class.java
        }
    }

    override fun showScreen(screenType: ScreenType) {
        Intent(context, screenTypeToActivityClass(screenType)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it)
        }
    }
}