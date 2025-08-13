package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.creator.PlayerCreator
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.search.domain.model.TrackMapper

class PlayerViewModel(track: Track?, private val player: AudioPlayer) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Stop)
    fun getStateLiveData(): LiveData<PlayerState> = stateLiveData

    private val trackPositionLiveData = MutableLiveData<String>()
    fun getTrackPositionLiveData(): LiveData<String> = trackPositionLiveData

    private val trackLiveData = MutableLiveData(track)
    fun getTrackLiveData(): LiveData<Track?> = trackLiveData

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    val timer = LooperTimer(mainThreadHandler, object : TimerTickListener {
        override fun onTickTimer() {
            trackPositionLiveData.postValue(TrackMapper.millisToMMSS(player.getCurrentPosition()))
        }

        override fun onResetTimer() {
            trackPositionLiveData.postValue("00:00")
        }
    })

    private val listener = object : AudioPlayer.Listener {
        override fun onReadyToPlay() {
            stateLiveData.postValue(PlayerState.ReadyToPlay)
        }

        override fun onPlay() {
            stateLiveData.postValue(PlayerState.Play)
            timer.start()
        }

        override fun onPause() {
            stateLiveData.postValue(PlayerState.Pause)
            timer.pause()
        }

        override fun onStop() {
            stateLiveData.postValue(PlayerState.Stop)
            timer.stop()
        }
    }

    init {
        player.setListener(listener)
        track?.previewUrl?.let { player.open(it) }
    }

    fun clickPlay() {
        if (player.isPlaying())
            player.pause()
        else
            player.play()
    }

    fun addCurrentTrackToFavourites() {
        //TODO: implement in a feature sprint
    }

    fun likeCurrentTrack() {
        //TODO: implement in a feature sprint
    }

    fun pause() {
        player.pause()
    }


    companion object {
        fun getFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track, PlayerCreator.getAudioPlayerProvider().provideAudioPlayer())
            }
        }
    }
}