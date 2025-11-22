package com.practicum.playlistmaker.playlist.ui.editor

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(
    val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    var playlistName = ""
    var playlistDescription = ""
    var imageUri: Uri? = null

    fun savePlaylist() {
        viewModelScope.launch {
            playlistInteractor.createCover(playlistName, playlistDescription, imageUri)
        }
    }

    fun canSavePlaylist() : Boolean {
        return playlistName.isNotBlank()
    }

    fun hasUserTypedText() : Boolean {
        return playlistName.isNotBlank() or playlistDescription.isNotBlank()
    }
}