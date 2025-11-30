package com.practicum.playlistmaker.playlist.ui.editor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.playlist.ui.PlaylistCoverViewModel
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(
    val playlistId: Int,
    val coversInteractor: CoversInteractor
): PlaylistCoverViewModel() {
    private val _coverLiveData = MutableLiveData<PlaylistCover>()
    val coverLiveData: LiveData<PlaylistCover> = _coverLiveData

    init {
        viewModelScope.launch {
            coversInteractor.subscribeToCover(playlistId).collect { cover ->
                cover?.let {
                    playlistName = it.title
                    playlistDescription = it.description
                    imageUri = Uri.parse(it.imagePath)
                    _coverLiveData.postValue(it)
                }
            }
        }
    }

    fun savePlaylist() {
        viewModelScope.launch {
            coversInteractor.updateCover(playlistId, playlistName, playlistDescription, imageUri)
        }
    }
}