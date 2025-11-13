package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun updateCover(id: Int, cover: PlaylistCover)
    suspend fun createCover(title: String, description: String, coverFullPath: Uri?): Boolean

    fun getPlaylists(): Flow<List<PlaylistCover>>
}