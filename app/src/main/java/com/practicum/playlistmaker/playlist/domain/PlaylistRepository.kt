package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.data.PlaylistEntry
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addCover(title: String, description: String, imagePath: String)
    fun getPlaylists(): Flow<List<PlaylistEntry>>
    fun getPlaylist(playlistId: Int): Flow<PlaylistEntry>
}