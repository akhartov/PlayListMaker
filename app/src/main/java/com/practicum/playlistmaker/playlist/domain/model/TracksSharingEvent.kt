package com.practicum.playlistmaker.playlist.domain.model

sealed interface TracksSharingEvent {
    data object NoTracksForSharing: TracksSharingEvent
}