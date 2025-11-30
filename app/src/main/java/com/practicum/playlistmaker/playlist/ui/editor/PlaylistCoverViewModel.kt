package com.practicum.playlistmaker.playlist.ui.editor

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import kotlinx.coroutines.launch

class PlaylistCoverViewModel(
    private val playlistId: Int,
    private val coversInteractor: CoversInteractor
) : ViewModel() {
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

    fun savePlaylist() {
        viewModelScope.launch {
          coversInteractor.updateCover(playlistId, playlistName, playlistDescription, imageUri)
        }
    }
}