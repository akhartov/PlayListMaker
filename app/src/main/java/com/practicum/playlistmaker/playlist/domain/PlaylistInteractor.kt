package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.SharedFlow

interface PlaylistInteractor {
    val playlistsFlow: SharedFlow<List<PlaylistCover>>

    suspend fun update()
    suspend fun createCover(title: String, description: String, coverFullPath: Uri?): Boolean
    suspend fun addTrackToPlaylist(playlistId: Int, track: Track)
}