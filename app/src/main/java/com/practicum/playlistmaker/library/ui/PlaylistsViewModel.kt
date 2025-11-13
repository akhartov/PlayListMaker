package com.practicum.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val playlistsLiveData = MutableLiveData<List<PlaylistCover>>()
    fun getPlaylistLiveData(): LiveData<List<PlaylistCover>> = playlistsLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { items ->
                playlistsLiveData.postValue(items)
            }
        }
    }
}