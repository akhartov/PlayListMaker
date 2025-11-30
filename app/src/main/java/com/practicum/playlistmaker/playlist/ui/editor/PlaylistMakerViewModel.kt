package com.practicum.playlistmaker.playlist.ui.editor

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import kotlinx.coroutines.launch

class PlaylistMakerViewModel(
    private val coversInteractor: CoversInteractor
) : ViewModel() {
    var playlistName = ""
    var playlistDescription = ""
    var imageUri: Uri? = null

    fun makePlaylistCover() {
        viewModelScope.launch {
            coversInteractor.makeCover(playlistName, playlistDescription, imageUri)
        }
    }

    fun canSavePlaylist() : Boolean {
        return playlistName.isNotBlank()
    }

    fun hasUserTypedText() : Boolean {
        return playlistName.isNotBlank() or playlistDescription.isNotBlank()
    }
}