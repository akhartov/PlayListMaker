package com.practicum.playlistmaker.playlist.domain.model

sealed interface PlaylistsEvent {
    class NewPlaylist(val title: String): PlaylistsEvent
}