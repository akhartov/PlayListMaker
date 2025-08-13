package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.main.creator.MainCreator
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

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(MainCreator.getScreenNevigator())
            }
        }
    }
}