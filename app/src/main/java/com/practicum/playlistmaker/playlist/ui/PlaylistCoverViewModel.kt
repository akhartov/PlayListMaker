package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import androidx.lifecycle.ViewModel

open class PlaylistCoverViewModel: ViewModel() {
    var playlistName = ""
    var playlistDescription = ""
    var imageUri: Uri? = null

    fun canSavePlaylist() : Boolean {
        return playlistName.isNotBlank()
    }

    fun hasUserTypedText() : Boolean {
        return playlistName.isNotBlank() or playlistDescription.isNotBlank()
    }
}