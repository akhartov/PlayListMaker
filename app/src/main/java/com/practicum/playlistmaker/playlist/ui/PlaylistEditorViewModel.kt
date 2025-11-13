package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(val playlistId: Int, val playlistInteractor: PlaylistInteractor): ViewModel() {
    fun savePlaylist(title: String, description: String, imagePath: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createCover(title, description, imagePath)
        }
    }
}