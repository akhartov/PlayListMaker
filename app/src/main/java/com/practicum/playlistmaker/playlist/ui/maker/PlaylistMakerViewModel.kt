package com.practicum.playlistmaker.playlist.ui.maker

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.ui.PlaylistCoverViewModel
import kotlinx.coroutines.launch

class PlaylistMakerViewModel(
    val coversInteractor: CoversInteractor
): PlaylistCoverViewModel() {
    fun makePlaylistCover() {
        viewModelScope.launch {
            coversInteractor.makeCover(playlistName, playlistDescription, imageUri)
        }
    }
}