package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.main.domain.ScreenNavigator
import com.practicum.playlistmaker.main.domain.ScreenType

class MainViewModel(private val screenNavigator: ScreenNavigator) : ViewModel() {
    fun showSearchScreen() {
        screenNavigator.showScreen(ScreenType.Search)
    }

    fun showSettingsScreen() {
        screenNavigator.showScreen(ScreenType.Settings)
    }

    fun showLibraryScreen() {
        screenNavigator.showScreen(ScreenType.Library)
    }
}