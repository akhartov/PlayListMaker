package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.library.ui.LibraryActivity
import com.practicum.playlistmaker.main.creator.MainCreator
import com.practicum.playlistmaker.main.domain.ScreenNavigator
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainViewModel(private val screenNavigator: ScreenNavigator) : ViewModel() {
    fun showSearchScreen() {
        screenNavigator.showScreen(SearchActivity::class.java)
    }

    fun showSettingsScreen() {
        screenNavigator.showScreen(SettingsActivity::class.java)
    }

    fun showLibraryScreen() {
        screenNavigator.showScreen(LibraryActivity::class.java)
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(MainCreator.getScreenNevigator())
            }
        }
    }
}