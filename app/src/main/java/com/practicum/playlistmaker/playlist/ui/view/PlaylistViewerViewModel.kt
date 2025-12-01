package com.practicum.playlistmaker.playlist.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.SharingInteractor
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistMessagingCache
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.model.TracksSharingEvent
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaylistViewerViewModel(
    private val playlistId: Int,
    private val coversInteractor: CoversInteractor,
    private val sharingInteractor: SharingInteractor,
    private var playlistMessagingCache: PlaylistMessagingCache
) : ViewModel() {

    private val _coverLiveData = MutableLiveData<PlaylistCover>()
    val coverLiveData: LiveData<PlaylistCover> = _coverLiveData

    private val _sharingLiveData = SingleLiveEvent<TracksSharingEvent>()
    val sharingLiveData: LiveData<TracksSharingEvent> = _sharingLiveData

    private var _tracksLiveData = MutableLiveData<List<Track>>()
    var tracksLiveData: LiveData<List<Track>> = _tracksLiveData

    init {
        viewModelScope.launch {
            coversInteractor.getTracksFlow(playlistId).collect { tracks ->
                _tracksLiveData.postValue(tracks)
                playlistMessagingCache.update(tracks)
            }
        }

        viewModelScope.launch {
            coversInteractor.getCoverFlow(playlistId).collect { cover ->
                cover?.let {
                    _coverLiveData.postValue(it)
                    playlistMessagingCache.update(it)
                }
            }
        }
    }

    fun deleteTrack(playlistId: Int, track: Track) {
        viewModelScope.launch {
            coversInteractor.deleteTrack(playlistId, track.trackId)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            coversInteractor.deleteCover(playlistId)
        }
    }

    fun sharePlaylist() {
        if (playlistMessagingCache.hasTracks()) {
            sharingInteractor.shareCustomText(playlistMessagingCache.makeMessage())
        } else {
            _sharingLiveData.postValue(TracksSharingEvent.NoTracksForSharing)
        }
    }
}