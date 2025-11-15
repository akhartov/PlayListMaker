package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val database: AppDatabase
) : PlaylistRepository {
    override suspend fun addCover(title: String, description: String?, imagePath: String?) {
        database.playlistDao().insertCover(PlaylistEntity(0, title, description.toString(), imagePath.toString()))
    }

    override fun getPlaylists(): Flow<List<PlaylistEntity>> {
        return database.playlistDao().getItems()
    }
}