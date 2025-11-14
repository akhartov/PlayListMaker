package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(val playlistId: Int, val trackId: Int, val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val playlistLiveData = MutableLiveData<PlaylistCover>()
    fun getPlaylistLiveData(): LiveData<PlaylistCover> = playlistLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylist(playlistId).collect {
                playlistLiveData.postValue(it)
            }
        }
    }
    fun savePlaylist(title: String, description: String, imagePath: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createCover(title, description, imagePath)
        }
    }
}