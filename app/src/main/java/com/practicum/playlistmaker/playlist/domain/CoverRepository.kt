package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import kotlinx.coroutines.flow.Flow

interface CoverRepository {
    suspend fun addCover(title: String, description: String?, imagePath: String?)
    suspend fun updateCover(playlistId: Int, title: String, description: String, fileName: String?)
    suspend fun deleteCover(playlistId: Int)

    fun getCovers(): Flow<List<PlaylistCover>>
    fun getCover(playlistId: Int): Flow<PlaylistCover?>
    fun getCoversWithStatistics(): Flow<List<PlaylistCover>>
    fun getCoverWithStatistics(playlistId: Int): Flow<PlaylistCover?>
}