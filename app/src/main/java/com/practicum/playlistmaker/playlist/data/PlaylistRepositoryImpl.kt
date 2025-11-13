package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val database: AppDatabase,
    private val mapper: PlaylistMapper
): PlaylistRepository {
    override suspend fun addCover(title: String, description: String, imagePath: String) {
        database.playlistDao().insertCover(PlaylistEntry(0, title, description, imagePath))
    }

    override fun getPlaylists(): Flow<List<PlaylistEntry>> {
        return database.playlistDao().getItems()
    }
}