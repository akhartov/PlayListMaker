package com.practicum.playlistmaker.player.domain.api

sealed interface TrackPlayingState {
    object NoTrack: TrackPlayingState
    object ReadyToPlay: TrackPlayingState
    object Completed: TrackPlayingState
}